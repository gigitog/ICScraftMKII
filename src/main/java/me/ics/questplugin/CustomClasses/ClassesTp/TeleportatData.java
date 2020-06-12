package me.ics.questplugin.CustomClasses.ClassesTp;

public class TeleportatData {
    public String nameTpWarp;
    public Integer x;
    public Integer y;
    public Integer z;
    public Integer xtp;
    public Integer ytp;
    public Integer ztp;

    public TeleportatData() {
        this.nameTpWarp = "";
        this.x = 0;
        this.y = 0;
        this.z = 0;
        this.xtp = 0;
        this.ytp = 0;
        this.ztp = 0;
    }

    public TeleportatData(String nameTpWarp, Integer x, Integer y, Integer z, Integer xtp, Integer ytp, Integer ztp) {
        this.nameTpWarp = nameTpWarp;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xtp = xtp;
        this.ytp = ytp;
        this.ztp = ztp;
    }
}
