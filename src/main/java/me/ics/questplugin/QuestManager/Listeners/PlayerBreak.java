package me.ics.questplugin.QuestManager.Listeners;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerBreak implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEntityEvent event){
        boolean is_frame = event.getRightClicked().getType().equals(EntityType.ITEM_FRAME);

        if(is_frame && event.getPlayer().getGameMode().equals(GameMode.ADVENTURE)){
            event.setCancelled(true);
        }
    }
    @EventHandler
    public void onLeftClick(EntityDamageByEntityEvent event){
        if(!(event.getDamager() instanceof Player))
            return;

        boolean is_frame = event.getEntity().getType().equals(EntityType.ITEM_FRAME);
        Player player = (Player) event.getDamager();
        if(is_frame && player.getGameMode().equals(GameMode.ADVENTURE)){
            event.setCancelled(true);
        }
    }
}
