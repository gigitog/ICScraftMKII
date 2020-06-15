package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.QuestPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Bed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class PlayerPlace implements Listener {

    public PlayerPlace(Plugin plugin) {

    }

    @EventHandler
    public void PlayerPlace(BlockPlaceEvent event){
        ItemStack item = event.getItemInHand();
        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta.getDisplayName().equalsIgnoreCase("§9Вернуться в лобби §7(ПКМ)")){
            event.setCancelled(true);
        }
    }
}
