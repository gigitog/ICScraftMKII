package me.ics.questplugin.CustomClasses.ClassesTxt;

import java.util.ArrayList;
import java.util.List;

public class ListTxtWarpData {
    public List<TxtWarpData> allData;

    public ListTxtWarpData(List<TxtWarpData> allData) {
        this.allData = allData;
    }

    public TxtWarpData getWarpByCheck(int checkpoint){
        for(TxtWarpData txtWarp : allData){
            if(txtWarp.index == checkpoint){
                return txtWarp;
            }
        }
        return null;
    }

    public ListTxtWarpData() {
        this.allData = new ArrayList<>();
    }
}

