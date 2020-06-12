package me.ics.questplugin.Buttons;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTp.ListTeleportsData;
import me.ics.questplugin.CustomClasses.ClassesTp.TeleportatData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class ListenerButton implements Listener {
    private Plugin plugin;

    public ListenerButton(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onButton(PlayerInteractEvent event){
        if(!event.hasBlock()) return;
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) &&
                Objects.requireNonNull(event.getClickedBlock()).
                        getType().equals(Material.STONE_BUTTON)){

            Player player = event.getPlayer();
            Location loc = event.getClickedBlock().getLocation();
            Location locTp = new Location(event.getPlayer().getWorld(),0,0,0, 0, 0);
            // editor
            FileJsonEditor<ListTeleportsData> editor = new FileJsonEditor<>(
                    "/buttons_data.json", new ListTeleportsData(), plugin);
            //list
            ListTeleportsData buttons = editor.getData();
            // search
            for(TeleportatData button : buttons.allData){
                // condition that player clicked on button in file
                boolean isHere = loc.getBlockX() == button.x &&
                        loc.getBlockY() == button.y && loc.getBlockZ() == button.z;
                if(isHere){
                    // set Location, Yaw and Pitch then tp
                    locTp.add(button.xtp + 0.5, button.ytp, button.ztp + 0.5);
                    locTp.setYaw(loc.getYaw());
                    locTp.setPitch(loc.getPitch());
                    player.teleport(locTp);
                }
            }
        }
    }
}
