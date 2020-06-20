package me.ics.questplugin.QuestManager.Commands;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.AnswerDataMap;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class AddAnswer implements CommandExecutor {
    private FileJsonEditor<AnswerDataMap> editor;

    public AddAnswer(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new AnswerDataMap(), plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!sender.isOp()){
            sender.sendMessage("no perms!");
            return true;
        }
        if(args.length < 2)
            return false;
        try{
            int index =  args[0].length() + 1;
            String answer = String.join(" ", args).substring(index);
            AnswerDataMap answers = editor.getData();
            answers.add(Integer.parseInt(args[0]), answer);
            editor.setData(answers);
        } catch (NumberFormatException e){
            return false;
        }
        return true;
    }
}
