package me.ics.questplugin.CustomClasses.ClassesButton;

public class ButtonData {
    public String nameTpWarp;
    public int index;
    public int value;
    public int checkpoint;
    public int x;
    public int y;
    public int z;
    public int xtp;
    public int ytp;
    public int ztp;

    public ButtonData() {
        this.nameTpWarp = "";
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.xtp = 0;
        this.ytp = 0;
        this.ztp = 0;
        this.index = -1;
        this.value = -1;
        this.checkpoint = 0;
    }

    public ButtonData(String nameTpWarp, int x, int y, int z, int index, int value, int checkpoint, int xtp, int ytp, int ztp) {
        this.checkpoint = checkpoint;
        this.nameTpWarp = nameTpWarp;
        this.x = x;
        this.y = y;
        this.z = z;
        this.value = value;
        this.index = index;
        this.xtp = xtp;
        this.ytp = ytp;
        this.ztp = ztp;
    }
}
