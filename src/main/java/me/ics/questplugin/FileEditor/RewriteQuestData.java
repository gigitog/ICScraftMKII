package me.ics.questplugin.FileEditor;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;

public class RewriteQuestData {

    public static void rewrite(ListQuestWorldData list, QuestWorldData dataToWrite){
        if (dataToWrite != null){
            int index = list.allQuestWorlds.indexOf(list.getQWDbyPlayer(dataToWrite.playerName));

            list.allQuestWorlds.set(index, dataToWrite);
//            editor.setData(data);
        }
    }

}
