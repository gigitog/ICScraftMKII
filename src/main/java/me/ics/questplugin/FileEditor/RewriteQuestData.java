package me.ics.questplugin.FileEditor;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;

public class RewriteQuestData {

    public static void rewrite(FileJsonEditor<ListQuestWorldData> editor, QuestWorldData dataToWrite){
        if (dataToWrite != null){
            ListQuestWorldData data = editor.getData();

            int index = data.allQuestWorlds.indexOf(data.getQWDbyPlayer(dataToWrite.playerName));

            data.allQuestWorlds.set(index, dataToWrite);
            editor.setData(data);
        }
    }

}
