package me.ics.questplugin.QuestManager.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PlayerInventoryInteract implements Listener {
    private List<String> noInteractItems = Arrays.asList("§aНачать квест §7(ПКМ)","§aИнформация о квесте §7(ПКМ)","§9Вернуться в лобби §7(ПКМ)");
    public PlayerInventoryInteract(){

    }
    @EventHandler
    public void onPlayerInventoryInteract(InventoryClickEvent event){
        if(event.getCurrentItem()!=null && noInteractItems.contains(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName())){
            event.setCancelled(true);
        }
    }
}
