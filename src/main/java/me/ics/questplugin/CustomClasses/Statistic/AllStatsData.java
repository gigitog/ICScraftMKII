package me.ics.questplugin.CustomClasses.Statistic;


import java.util.List;

public class AllStatsData {
    private int index;
    private String playerName;
    private int[] votes;
    private List<Integer> tasksComplete;
    private int[] resultPoints;

    public AllStatsData(int index, String playerName, int[] votes, List<Integer> tasksComplete, int[] resultPoints) {
        this.index = index;
        this.playerName = playerName;
        this.votes = votes;
        this.tasksComplete = tasksComplete;
        this.resultPoints = resultPoints;
    }
}
