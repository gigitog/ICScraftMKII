package me.ics.questplugin.FileEditor;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Scanner;

public class FileJsonEditor<T> {
    private File file;
    private T data;

    public FileJsonEditor(String nameFile, T zeroData, Plugin plugin) {
        this.file = new File(plugin.getDataFolder().getAbsolutePath() + nameFile);
        this.data = zeroData;
        load(zeroData);
    }

    public T getData() {
        load(data);
        return data;
    }

    public void setData(T newData) {
        load(data);
        inFileJson(newData, new Gson());
    }

    private void load(T zeroData){
        try {
            if(!file.exists()) {
                file.createNewFile();
                // Создаю переменную с нулевыми полям
                inFileJson(zeroData, new Gson());
            }
            String jsonText = "";
            try{
                FileReader fileReader = new FileReader(file);
                Scanner scanner = new Scanner(fileReader);
                while(scanner.hasNextLine())
                    jsonText = jsonText.concat(scanner.nextLine());
                fileReader.close();
            }catch (IOException e){
                System.out.println("!!!Error " + e);
            }

            Gson g = new Gson();
            data = g.fromJson(jsonText, (Type) data.getClass());
        } catch (JsonParseException | IOException e) {
            System.out.println("!!!Error " + e);
        }
    }

    private void inFileJson(T dataToWrite, Gson g) {
        String s = g.toJson(dataToWrite, dataToWrite.getClass());
        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(s);
        } catch (IOException e) {
            System.out.println("!!!Error " + e);
        }
    }
}