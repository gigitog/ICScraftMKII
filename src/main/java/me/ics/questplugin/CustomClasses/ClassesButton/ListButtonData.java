package me.ics.questplugin.CustomClasses.ClassesButton;

import me.ics.questplugin.CustomClasses.ClassesTp.TeleportatData;

import java.util.ArrayList;
import java.util.List;

public class ListButtonData {
    public List<ButtonData> allData;

    public ListButtonData(List<ButtonData> allData) {
        this.allData = allData;
    }

    public ListButtonData() {
        this.allData = new ArrayList<>();
    }
}
