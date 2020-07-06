package me.ics.questplugin.CustomClasses.ClassesQuestWorld;

import org.bukkit.World;

import java.util.ArrayList;

public class QuestWorldData {
    public String questWorldName;
    public String playerName;
    public boolean isBusy;
    public int ticksLivedWhenStart;
    public int ticksSavedBeforeLeaving;
    public int ticksPlayedFinal;
    public int counter;
    public double[] spawn;
    public int checkpoint;
    public ArrayList<Integer> num_quests_complete;
    public int[] votes = new int[6];

    public QuestWorldData(World questWorld) {
        this.questWorldName = questWorld.getName();
        this.playerName = "";
        this.isBusy = false;
        this.ticksLivedWhenStart = 0;
        this.ticksSavedBeforeLeaving = 0;
        this.ticksPlayedFinal = 0;
        this.counter = 0;
        this.checkpoint = 0;
        this.num_quests_complete = new ArrayList<>();
        this.spawn = new double[]{679.5, 69, 469.5};
    }
}
