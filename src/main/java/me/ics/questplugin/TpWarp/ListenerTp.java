package me.ics.questplugin.TpWarp;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTp.ListTeleportsData;
import me.ics.questplugin.CustomClasses.ClassesTp.TeleportatData;
import me.ics.questplugin.HelpClasses.PlayerChecker;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

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

        if (PlayerChecker.isNotInQuest(player)) return;
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
                double x = loc.getX() - loc.getBlockX();
                double y = loc.getY() - loc.getBlockY();
                double z = loc.getZ() - loc.getBlockZ();
                locTp.add(tpWarp.xtp + x, tpWarp.ytp + y, tpWarp.ztp + z);
                locTp.setYaw(loc.getYaw());
                locTp.setPitch(loc.getPitch());
                // set Location, Yaw and Pitch then tp
                if ( tpWarp.x == 600 && tpWarp.z == 384 && tpWarp.y == 66 ){
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            Location locMCtp = new Location(player.getWorld(), 600.5, 69.1, 370.5);
                            player.teleport(locMCtp);
                        }
                    }.runTaskLater(plugin, 280);
                }
                player.teleport(locTp);
            }
        }
    }
}
