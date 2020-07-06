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
            {2, 1, 3, 1, 4, 1},
            {2, 2, 4, 1, 1, 2},
            {2, 2, 3, 2, 2, 1}
    };

    public void writeStats(){
        ListAllStatsData stats = editor.getData();
        AllStatsData newPlayerStats = new AllStatsData(stats.allData.size(), playerName, array, resultSpecialities(), percentage(), resultPoints());

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

    public List<Integer> resultSpecialities(){
        int max = 0;
        List<Integer> process = new ArrayList<>();
        List<Integer> max_indexes = new ArrayList<>();
        max_indexes.add(0);
        max_indexes.add(0);
        max_indexes.add(0);

        for (int i : resultPoints()) {
            process.add(i);
        }

        for (int i = 0; i < 3; i++) {
            for (int j : process){
                if (max <= j && !max_indexes.contains(process.indexOf(j))) {
                    max = j;
                    max_indexes.set(i, process.indexOf(j));
                }
            }
        }
        List<Integer> specialities = new ArrayList<>();
        for (int i : max_indexes) specialities.add(indexToSpec(i));

        return max_indexes;
    }

    private int indexToSpec(int i){
        int spec = 122;
        switch (i){
            case 0: spec = 113; break;
            case 1: spec = 121; break;
            case 2: spec = 122; break;
            case 3: spec = 123; break;
            case 4: spec = 126; break;
            case 5: spec = 151; break;
        }
        return spec;
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
