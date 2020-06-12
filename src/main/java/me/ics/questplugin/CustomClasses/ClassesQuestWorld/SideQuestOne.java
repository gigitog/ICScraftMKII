package me.ics.questplugin.CustomClasses.ClassesQuestWorld;

import org.bukkit.block.data.type.NoteBlock;

public class SideQuestOne {
    public Integer ticks;
    public NoteBlock first = null;
    public NoteBlock second = null;
    public NoteBlock third = null;
    public NoteBlock fourth = null;
    public Integer ticksEnd = 0;

    public SideQuestOne(Integer ticks) {
        this.ticks = ticks;

    }
//
//    public SideQuestOne(Integer ticks) {
//        this.ticks = ticks;
//        this.first = first;
//        this.second = second;
//        this.third = 0;
//        this.fourth = 0;
//        this.ticksEnd = 0;
//    }
}