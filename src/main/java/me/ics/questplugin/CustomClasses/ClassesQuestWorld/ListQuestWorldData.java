package me.ics.questplugin.CustomClasses.ClassesQuestWorld;

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
}
