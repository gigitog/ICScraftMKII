package me.ics.questplugin.HelpClasses;

import org.bukkit.Instrument;
import org.bukkit.Note;

public class SoundClass {
    public int octave;
    public String tone;
    public String instrument;

    public SoundClass(Note note, Instrument instrument) {
        this.octave = note.getOctave();
        this.tone = note.getTone().name();
        this.instrument = instrument.name();
    }
}
