package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.QuestPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class PlayerMoveItems implements Listener {
    public PlayerMoveItems(Plugin plugin) {

    }
    @EventHandler
    public void onPlayerMoveItem(InventoryClickEvent event){
        ItemStack item = event.getCurrentItem();
        List<String> itemsNotToMove = Arrays.asList("§aИнформация о квесте §7(ПКМ)","§9Вернуться в лобби §7(ПКМ)","§aНачать квест §7(ПКМ)");
        List<InventoryAction> actionsNotAllowed = Arrays.asList(InventoryAction.MOVE_TO_OTHER_INVENTORY,InventoryAction.PICKUP_SOME,InventoryAction.PICKUP_ALL,InventoryAction.PICKUP_ONE,InventoryAction.PICKUP_HALF,InventoryAction.CLONE_STACK,InventoryAction.HOTBAR_MOVE_AND_READD,InventoryAction.HOTBAR_SWAP,InventoryAction.SWAP_WITH_CURSOR);
        if(actionsNotAllowed.contains(event.getAction())){
            if(itemsNotToMove.contains(item.getItemMeta().getDisplayName())){
                event.setCancelled(true);
            }
        }
    }
}
