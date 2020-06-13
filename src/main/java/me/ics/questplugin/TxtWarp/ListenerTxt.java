package me.ics.questplugin.TxtWarp;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTxt.ListTxtWarpData;
import me.ics.questplugin.CustomClasses.ClassesTxt.TxtWarpData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public class ListenerTxt implements Listener {
    private Map<String, Boolean> places = new HashMap<>();
    private FileJsonEditor<ListTxtWarpData> editor;

    public ListenerTxt(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListTxtWarpData(), plugin);
    }


    private boolean placesContain(String name_place){
        if(places.get(name_place) == null)
            return false;
        return places.get(name_place);
    }
    // add playerName_placeName to map and make it True
    private void add(String name_place){
        places.put(name_place, true);
    }
    // playerName_placeName set false
    private void remove(String name_place){
        places.put(name_place, false);
    }
    @EventHandler
    public void onTxtWarp(PlayerMoveEvent event){
        Player player = event.getPlayer();
        Location loc = event.getTo();

        ListTxtWarpData tempData = editor.getData();
        for(TxtWarpData txtWarp : tempData.allData){
            assert loc != null;
            boolean x = Math.abs(loc.getBlockX() - txtWarp.x) <= txtWarp.radius;
            boolean y = Math.abs(loc.getBlockY() - txtWarp.y) <= 1;
            boolean z = Math.abs(loc.getBlockZ() - txtWarp.z) <= txtWarp.radius;
            // get name of the place of the iteration
            // make "name"_"place" name - nick, place - name of the area
            String name_place = player.getName().concat("_".concat(txtWarp.name));
            if(x && y && z){
                if(!placesContain(name_place)){
                    //if it's player's first step in the region, make name_place in map true
                    add(name_place);
                    //send text that is in file
                    player.sendMessage(color(txtWarp.text));
                }
            } else {
                if(placesContain(name_place)){
                    //if coords are incorrect, else statement is working. So we need
                    //to set "name"_"place" to "false" in the HashMap
                    remove(name_place);
                }
            }
        }
    }

    private String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}