package me.ics.questplugin.CustomClasses.ClassesQuestWorld;

import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class QuestWorldData {
    public String questWorldName;
    public String playerName;
    public boolean isBusy;
    public int ticksLivedWhenStart;
    public int ticksSavedBeforeLeaving;
    public int ticksPlayedFinal;
    public int shownMessages;
    public double[] spawn;
    public List<Integer> num_quests_complete;

    public QuestWorldData(World questWorld) {
        this.questWorldName = questWorld.getName();
        this.playerName = "";
        this.isBusy = false;
        this.ticksLivedWhenStart = 0;
        this.ticksSavedBeforeLeaving = 0;
        this.ticksPlayedFinal = 0;
        this.shownMessages = 0;
        this.num_quests_complete = new ArrayList<>();
        this.spawn = new double[]{193.5, 78, 428.5};
    }
}
