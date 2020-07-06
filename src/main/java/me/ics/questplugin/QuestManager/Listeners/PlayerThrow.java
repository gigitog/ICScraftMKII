package me.ics.questplugin.QuestManager.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerThrow implements Listener {

    @EventHandler
    public void onPlayerThrow(PlayerDropItemEvent event){
        ItemStack item = event.getItemDrop().getItemStack();
        ItemMeta meta = item.getItemMeta();
        String itemDisplayName = meta.getDisplayName();
        Player player = event.getPlayer();
        if (player.isOp()) return;

        if(itemDisplayName.equalsIgnoreCase("§9Вернуться в лобби §7(ПКМ)")){
            event.setCancelled(true);
        }
        if(itemDisplayName.equalsIgnoreCase("§aНачать квест §7(ПКМ)")){
            ItemStack temp = item.clone();
            player.getInventory().setItem(4, temp);
            item.setAmount(0);
        }
        if(itemDisplayName.equalsIgnoreCase("§aИнформация о квесте §7(ПКМ)")){
            ItemStack temp = item.clone();
            player.getInventory().setItem(7, temp);
            item.setAmount(0);
        }
    }
}
