package me.ics.questplugin.FileEditor;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;

public class RewriteDataInCycle {

    public void rewrite(int index, QuestWorldData dataToWrite, FileJsonEditor<ListQuestWorldData> editor, boolean checkpointWas){
        if(checkpointWas){
            ListQuestWorldData data = editor.getData();
            data.allQuestWorlds.remove(index);
            data.allQuestWorlds.add(index, dataToWrite);
            editor.setData(data);
        }
    }
}
