package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteQuestData;
import me.ics.questplugin.HelpClasses.QuestInstruments;
import me.ics.questplugin.QuestManager.Scoreboards.ScoreBoardQuest;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

public class PlayerTeleport implements Listener {
    private FileJsonEditor<ListQuestWorldData> editor;
    private ListQuestWorldData listQuestWorldData;
    private Plugin plugin;

    public PlayerTeleport(Plugin plugin, String fileName, ListQuestWorldData listQuestWorldData) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
        this.plugin = plugin;
        this.listQuestWorldData = listQuestWorldData;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event){
        Location from = event.getFrom();
        Location to = event.getTo();
        Player player = event.getPlayer();
        QuestWorldData questWorldData = listQuestWorldData.getQWDbyPlayer(player.getName());
        if(questWorldData == null) return;

        if(!from.getWorld().equals(to.getWorld()) && from.getWorld().getName().startsWith("quest")){
//            if(questWorldData.ticksPlayedFinal != 0){
//                ScoreBoardQuest.scoreONPU(plugin, player, editor);
//                return;
//            }
            ScoreBoardQuest.scoreONPU(plugin, player, listQuestWorldData);
            player.getInventory().clear();
            if (questWorldData.checkpoint == 1050){
                questWorldData.checkpoint = 1111;
            }
            player.getInventory().setItem(4, QuestInstruments.makeQuestBook());
            player.getInventory().setItem(8, QuestInstruments.makeLobbyBed());

            questWorldData.ticksSavedBeforeLeaving += player.getTicksLived()-questWorldData.ticksLivedWhenStart;
            questWorldData.ticksLivedWhenStart = 0;
            questWorldData.spawn = new double[]{from.getX(),from.getY(),from.getZ()};
            RewriteQuestData.rewrite(listQuestWorldData, questWorldData);
            return;
        }
        if(!from.getWorld().equals(to.getWorld()) && to.getWorld().getName().startsWith("quest")){
            if (questWorldData.ticksPlayedFinal != 0) {
                event.setCancelled(true);
            }

            new ScoreBoardQuest().scoreQuest(listQuestWorldData, plugin, player);
            player.getInventory().clear();
            player.getInventory().setItem(7, QuestInstruments.makeEndRedstone());
            player.getInventory().setItem(8, QuestInstruments.makeLobbyBed());

            questWorldData.ticksLivedWhenStart = player.getTicksLived();
            RewriteQuestData.rewrite(listQuestWorldData, questWorldData);
        }
    }
}
