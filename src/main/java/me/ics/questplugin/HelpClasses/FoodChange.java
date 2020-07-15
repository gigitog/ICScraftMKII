package me.ics.questplugin.HelpClasses;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class FoodChange implements Listener {

    @EventHandler
    private void onFood(FoodLevelChangeEvent event){
        if (!event.getEntity().getWorld().getName().equals("Survival")){
            event.setCancelled(true);
        }
    }
}
