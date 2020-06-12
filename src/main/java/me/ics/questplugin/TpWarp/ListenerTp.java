package me.ics.questplugin.TpWarp;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTp.ListTeleportsData;
import me.ics.questplugin.CustomClasses.ClassesTp.TeleportatData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class ListenerTp implements Listener {
    private Plugin plugin;

    public ListenerTp(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onTpWarp(PlayerMoveEvent event){
        Location loc = event.getTo();
        Location locTp = new Location(event.getPlayer().getWorld(),0,0,0, 0, 0);
        Player player = event.getPlayer();
        // editor
        FileJsonEditor<ListTeleportsData> editor = new FileJsonEditor<>(
                "/tp_warps_data.json", new ListTeleportsData(), plugin);
        //list
        ListTeleportsData tpWarps = editor.getData();
        // search
        for(TeleportatData tpWarp : tpWarps.allData){
            assert loc != null;
            // condition that player is on warp
            boolean isHere = loc.getBlockX() == tpWarp.x &&
                    loc.getBlockY() == tpWarp.y && loc.getBlockZ() == tpWarp.z;
            if(isHere){
                // set Location, Yaw and Pitch then tp
                locTp.add(tpWarp.xtp + 0.5, tpWarp.ytp, tpWarp.ztp + 0.5);
                locTp.setYaw(loc.getYaw());
                locTp.setPitch(loc.getPitch());
                player.teleport(locTp);
            }
        }
    }
}
