package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.HelpClasses.QuestInstruments;
import me.ics.questplugin.QuestManager.Scoreboards.ScoreBoardQuest;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class PlayerLogin implements Listener {
    List<String> strings = Arrays.asList("§aНачать квест §7(ПКМ)","§aИнформация о квесте §7(ПКМ)","§9Вернуться в лобби §7(ПКМ)");
    private FileJsonEditor<ListQuestWorldData> editorQuest;
    private Plugin plugin;
    private ListQuestWorldData listQuestWorldData;

    public PlayerLogin(Plugin plugin, String fileNameQuest, ListQuestWorldData listQuestWorldData) {
        editorQuest = new FileJsonEditor<>(fileNameQuest, new ListQuestWorldData(), plugin);
        this.plugin = plugin;
        this.listQuestWorldData = listQuestWorldData;
    }


    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event){
        Player player = event.getPlayer();

        if (player.getWorld().getName().equals("Survival")) return;
        QuestWorldData qwd = listQuestWorldData.getQWDbyPlayer(player.getName());
        if (qwd == null || qwd.ticksPlayedFinal == 0){

            player.getInventory().setItem(8, QuestInstruments.makeLobbyBed());
        } else {
            ScoreBoardQuest.scoreONPU(plugin, player, listQuestWorldData);
            return;
        }
        if (player.getWorld().getName().startsWith("quest")){
            new ScoreBoardQuest().scoreQuest(listQuestWorldData, plugin, player);
            player.getInventory().setItem(7, QuestInstruments.makeEndRedstone());
            player.getInventory().setItem(4, new ItemStack(Material.AIR));
        } else {
            player.getInventory().setItem(4, QuestInstruments.makeQuestBook());
            ScoreBoardQuest.scoreONPU(plugin, player, listQuestWorldData);
        }

        if (player.isOp()) {
            player.getInventory().setItem(0, QuestInstruments.makeStatsFeather());
        }
    }
}
