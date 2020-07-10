package me.ics.questplugin.QuestManager.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class PlayerThrow implements Listener {

    List<String> itemsNotToDrop = Arrays.asList("§9Вернуться в лобби §7(ПКМ)","§aНачать квест §7(ПКМ)","§aИнформация о квесте §7(ПКМ)","§aЗакончить квест §7(ПКМ)");

    @EventHandler
    public void onPlayerThrow(PlayerDropItemEvent event){
        String itemDisplayName = event.getItemDrop().getItemStack().getItemMeta().getDisplayName();
        if(itemsNotToDrop.contains(itemDisplayName)){
            event.setCancelled(true);
        }
    }
}
