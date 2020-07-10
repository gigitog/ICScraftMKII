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

import java.util.Arrays;
import java.util.List;

public class PlayerPlace implements Listener {

    List<String> list = Arrays.asList("§aЗакончить квест §7(ПКМ)","§9Вернуться в лобби §7(ПКМ)");

    public PlayerPlace(Plugin plugin) {

    }

    @EventHandler
    public void PlayerPlace(BlockPlaceEvent event){
        ItemStack item = event.getItemInHand();
        ItemMeta itemMeta = item.getItemMeta();
        if(list.contains(itemMeta.getDisplayName())){
            event.setCancelled(true);
        }
    }
}
