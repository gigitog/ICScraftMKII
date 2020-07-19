package me.ics.questplugin.QuestManager.Listeners;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class PlayerPlace implements Listener {
    List<String> list = Arrays.asList("§aЗакончить квест §7(ПКМ)","§9Вернуться в лобби §7(ПКМ)");

    @EventHandler
    public void PlayerPlace(BlockPlaceEvent event){
        ItemStack item = event.getItemInHand();
        ItemMeta itemMeta = item.getItemMeta();
        if(list.contains(itemMeta.getDisplayName())){
            event.setCancelled(true);
        }
    }
}
