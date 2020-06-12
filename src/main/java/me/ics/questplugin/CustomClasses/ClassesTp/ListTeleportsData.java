package me.ics.questplugin.CustomClasses.ClassesTp;

import java.util.ArrayList;
import java.util.List;

public class ListTeleportsData {
    public List<TeleportatData> allData;

    public ListTeleportsData(List<TeleportatData> allData) {
        this.allData = allData;
    }

    public ListTeleportsData() {
        this.allData = new ArrayList<>();
    }
}
