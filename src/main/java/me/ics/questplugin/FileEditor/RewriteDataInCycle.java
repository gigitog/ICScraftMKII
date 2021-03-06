package me.ics.questplugin.FileEditor;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;

public class RewriteDataInCycle {

    public void rewrite(int index, QuestWorldData dataToWrite, FileJsonEditor<ListQuestWorldData> editor, boolean checkpointWas){
        if(checkpointWas && dataToWrite != null){
            ListQuestWorldData data = editor.getData();
            data.allQuestWorlds.set(index, dataToWrite);
            editor.setData(data);
        }
    }
}
