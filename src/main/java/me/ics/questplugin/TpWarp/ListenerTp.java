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
import org.bukkit.scheduler.BukkitRunnable;

public class ListenerTp implements Listener {
    private Plugin plugin;
    private FileJsonEditor<ListTeleportsData> editor;
    private  ListTeleportsData tpWarps;

    public ListenerTp(Plugin plugin) {
        this.plugin = plugin;
        editor = new FileJsonEditor<>(
                "/tp_warps_data.json", new ListTeleportsData(), plugin);
        tpWarps = editor.getData();
    }

    @EventHandler
    public void onTpWarp(PlayerMoveEvent event){
        Location loc = event.getTo();

        Player player = event.getPlayer();
        // editor
        //list
        // search
        for(TeleportatData tpWarp : tpWarps.allData){
            assert loc != null;
            // condition that player is on warp
            boolean isHere = loc.getBlockX() == tpWarp.x &&
                    loc.getBlockY() == tpWarp.y && loc.getBlockZ() == tpWarp.z;
            if(isHere){
                Location locTp = new Location(event.getPlayer().getWorld(),0,0,0, 0, 0);
                double x = loc.getX() - loc.getBlockX();
                double y = loc.getY() - loc.getBlockY();
                double z = loc.getZ() - loc.getBlockZ();
                locTp.add(tpWarp.xtp + x, tpWarp.ytp + y, tpWarp.ztp + z);
                locTp.setYaw(loc.getYaw());
                locTp.setPitch(loc.getPitch());
                // set Location, Yaw and Pitch then tp 520,"y":66,"z":544
                if ( tpWarp.x == 520 && tpWarp.y == 66 && tpWarp.z == 544){
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            Location locMCtp = new Location(player.getWorld(), 520.5, 69.1, 531.5);
                            player.teleport(locMCtp);
                        }
                    }.runTaskLater(plugin, 200);
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            Location locMCtp = new Location(player.getWorld(), 582.5, 67.1, 304.5);
                            locMCtp.setYaw(-90);
                            player.teleport(locMCtp);
                        }
                    }.runTaskLater(plugin, 700);
                }
                player.teleport(locTp);
                return;
            }
        }
    }
}
