package me.ics.questplugin.CustomClasses.Statistic;

import me.ics.questplugin.FileEditor.FileJsonEditor;

import java.util.ArrayList;
import java.util.List;

public class ArrayProcessor {
    private FileJsonEditor<ListAllStatsData> editor;
    private int[] array;
    private String playerName;

    public ArrayProcessor(FileJsonEditor<ListAllStatsData> editor, int[] array, String playerName) {
        this.editor = editor;
        this.array = array;
        this.playerName = playerName;
    }

    private int[][] table = {
            {3, 4, 2, 1, 0, 2},
            {3, 2, 4, 3, 1, 2},
            {3, 3, 4, 2, 2, 2},
            {3, 1, 3, 1, 4, 1},
            {2, 2, 4, 1, 1, 2},
            {2, 2, 3, 2, 2, 1}
    };

    public void writeStats(){
        ListAllStatsData stats = editor.getData();
        AllStatsData newPlayerStats = new AllStatsData(stats.allData.size(), playerName, array, percentage(), resultPoints());

        stats.allData.add(newPlayerStats);
        editor.setData(stats);
    }

    public int[] resultPoints(){
        int[] result = new int[]{0, 0, 0, 0, 0, 0};
        int j = 0;
        for(int i = 0; i < 6; i++){
            for (int n : array){
                result[i] +=
                        n * table[i][j];
                j++;
            }
            j = 0;
        }

        return result;
    }

    public List<Integer> percentage(){
        int[] r = resultPoints();
        List<Integer> process = new ArrayList<>();
        for (int i : r) {
            process.add(sigmoid(i));
        }
        return process;
    }

    private static int sigmoid(int x){
        int res = x * 4;
        if (res  > 100 ) res = 100;
        return res;
    }
}
