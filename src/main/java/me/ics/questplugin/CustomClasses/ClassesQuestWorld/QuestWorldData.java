package me.ics.questplugin.CustomClasses.ClassesQuestWorld;

import org.bukkit.World;

public class QuestWorldData {
    public String questWorldName;
    public boolean isBusy;
    public String playerName;
    public int ticksLivedWhenStart;
    public int ticksSavedBeforeLeaving;
    public int ticksPlayedFinal;
    public int checkpoint;
    public int shownMessages;
//    public SideQuestOne sideQuestOne;
    public double[] spawn;

    public QuestWorldData(World questWorld) {
        this.questWorldName = questWorld.getName();
        this.isBusy = false;
        this.playerName = "";
        this.ticksLivedWhenStart = 0;
        this.ticksSavedBeforeLeaving = 0;
        this.ticksPlayedFinal = 0;
        this.checkpoint = 0;
        this.shownMessages = 0;
//        this.sideQuestOne = new SideQuestOne(0);
        this.spawn = new double[]{193.5, 78, 428.5};
    }
}
