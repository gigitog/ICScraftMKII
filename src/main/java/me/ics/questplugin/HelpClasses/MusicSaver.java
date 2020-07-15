package me.ics.questplugin.HelpClasses;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public class MusicSaver implements Listener {
    private Plugin plugin;
    private boolean isClick = false;
    private FileJsonEditor<MapMusic> editorMusic;
    private int ticks = 0;
    private Player playerMusic;
    Map<Integer, SoundClass> map = new TreeMap<>();
    private BukkitRunnable timer = new BukkitRunnable() {
        @Override
        public void run() {
            for(int key : map.keySet()){
                if(key==playerMusic.getTicksLived()-ticks){
                    playerMusic.sendMessage(map.get(key).instrument);
                    Instrument instrument = Instrument.valueOf(map.get(key).instrument);
                    Note note = Note.flat(map.get(key).octave, Note.Tone.valueOf(map.get(key).tone));
                    playerMusic.playNote(playerMusic.getLocation(),instrument,note);
                }
            }
        }
    };

    public MusicSaver(Plugin plugin){
        this.plugin = plugin;
        editorMusic = new FileJsonEditor<>("/music", new MapMusic(), plugin);
    }

    @EventHandler
    private void onMusicStart(PlayerInteractEvent event){
        if(!event.hasBlock()) return;
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) &&
                Objects.requireNonNull(event.getClickedBlock()).
                        getType().equals(Material.JUNGLE_BUTTON)) {

            Player player = event.getPlayer();
            playerMusic = player;
            ticks = player.getTicksLived();
            Location loc = event.getClickedBlock().getLocation();
            if (loc.getBlockX() == 600){
                player.sendMessage("Started recording");
                isClick = true;
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        isClick = false;
                    }
                }.runTaskLater(plugin, 399);
            }
            if(loc.getBlockX() == 602){
                onMusicPlayer();
            }
        }
    }

    @EventHandler
    private void onMusicSaver(NotePlayEvent event){
        if (!isClick) return;
        MapMusic m = editorMusic.getData();
        SoundClass soundClass = new SoundClass(event.getNote(),event.getInstrument());
        playerMusic.sendMessage("note");
        m.songMap.put(playerMusic.getTicksLived() - ticks, soundClass);
        editorMusic.setData(m);
    }

    private void onMusicPlayer(){
        ticks = playerMusic.getTicksLived();
        map = editorMusic.getData().songMap;
        timer.runTaskTimer(plugin,0,1);
    }

    public static class MapMusic{
        Map<Integer,SoundClass> songMap = new TreeMap<>();
    }
}