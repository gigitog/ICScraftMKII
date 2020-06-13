package me.ics.questplugin.CustomClasses.ClassesTxt;

public class TxtWarpData {
    public String name;
    public Integer x;
    public Integer y;
    public Integer z;
    public Integer radius;
    public String text;
    public int index;

    public TxtWarpData() {
        this.name = "";
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.radius = 0;
        this.text = "";
        this.index = 0;
    }

    public TxtWarpData(String name, Integer x, Integer y, Integer z, Integer radius, Integer is_quest, String text) {
        // index = -1 -> не квестовая текстовая точка
        this.index = is_quest;
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.radius = radius;
        this.text = text;
    }
}
