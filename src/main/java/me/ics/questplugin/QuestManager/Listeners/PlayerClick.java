package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.HelpClasses.QuestInstruments;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PlayerClick implements Listener {
    private FileJsonEditor<ListQuestWorldData> editor;

    public PlayerClick(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
    }

    @EventHandler
    public void onPlayerClick(PlayerInteractEvent event) {
        boolean is = (event.getAction().equals(Action.RIGHT_CLICK_AIR) && !event.hasBlock()) ||
                (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.hasBlock());
        Player player = event.getPlayer();
        ItemStack item = null;
        if (event.hasItem()) {
            item = event.getItem();
        }

        if (is && item != null) {
            if (item.getItemMeta().getDisplayName().equalsIgnoreCase("§9Вернуться в лобби §7(ПКМ)")) {
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
        QuestWorldData questWorldData = editor.getData().getQWDbyPlayer(player.getName());
        if (questWorldData == null || questWorldData.ticksPlayedFinal != 0)
            return;
        if (questWorldData.checkpoint < 100) {
            if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.hasBlock() && event.getClickedBlock().getType().equals(Material.PLAYER_HEAD)) {
                if (player.getFacing().getDirection().getX() > 0) {
                    player.sendTitle(ChatColor.GREEN + "Квест начат!", "", 30, 60, 10);
                    player.teleport(new Location(player.getWorld(), 679, 69.25, 469));
                    player.getInventory().setItem(7, QuestInstruments.makeEndRedstone());
                } else
                    player.sendTitle(ChatColor.RED + "Со стороны экрана", "", 10, 50, 10);
            }
        }
    }
}