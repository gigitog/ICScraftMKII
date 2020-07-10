package me.ics.questplugin.CustomClasses.ClassesQuestWorld;

import jdk.internal.jline.internal.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ListQuestWorldData {
    public List<QuestWorldData> allQuestWorlds;

    public ListQuestWorldData(List<QuestWorldData> allQuestWorlds) {
        this.allQuestWorlds = allQuestWorlds;
    }
    public ListQuestWorldData() {
        this.allQuestWorlds = new ArrayList<>();
    }

    @Nullable
    public QuestWorldData getQWDbyPlayer(String playerName){
        for (QuestWorldData qwd : allQuestWorlds){
            if (qwd.isBusy && qwd.playerName.equalsIgnoreCase(playerName)) {
                return qwd;
            }
        }
        return null;
    }
}
