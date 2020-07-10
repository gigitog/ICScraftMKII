package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteQuestData;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;


public class PlayerClick implements Listener {
    private FileJsonEditor<ListQuestWorldData> editor;

    public PlayerClick(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event){
        boolean is = (event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.hasBlock()) ||
                (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.hasBlock());
        Player player = event.getPlayer();
        if(event.getItem() == null)
            return;
        ItemStack item = event.getItem();
        // making a "list class" of quest worlds from file
        ListQuestWorldData listQuestWorldData = editor.getData();
        if(is) {
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§9Вернуться в лобби §7(ПКМ)")){
                player.performCommand("spawn");
                return;
            }
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§aНачать квест §7(ПКМ)")) {
                player.performCommand("quest");
                return;
            }
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§aЗакончить квест §7(ПКМ)")) {
                player.performCommand("finish");
                return;
            }
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§aИнформация о квесте §7(ПКМ)")) {
                player.performCommand("quest status " + player.getName());
            }
        }
    }
}