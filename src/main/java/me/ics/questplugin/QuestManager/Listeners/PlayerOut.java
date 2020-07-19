package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
import me.ics.questplugin.FileEditor.RewriteQuestData;
import me.ics.questplugin.HelpClasses.QuestInstruments;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class PlayerOut implements Listener {
    private FileJsonEditor<ListQuestWorldData> editor;
    private ListQuestWorldData listQuestWorldData;

    public PlayerOut(Plugin plugin, String fileName, ListQuestWorldData listQuestWorldData) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
        this.listQuestWorldData = listQuestWorldData;
    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent e){
        QuestWorldData questWorldData = listQuestWorldData.getQWDbyPlayer(e.getPlayer().getName());

        if(questWorldData == null) return;

        if(questWorldData.questWorldName.equalsIgnoreCase(e.getPlayer().getWorld().getName())){
            questWorldData.ticksSavedBeforeLeaving += e.getPlayer().getTicksLived() - questWorldData.ticksLivedWhenStart;
            questWorldData.ticksLivedWhenStart = 0;
            e.getPlayer().getInventory().clear();
            RewriteQuestData.rewrite(listQuestWorldData, questWorldData);
        }

        if (questWorldData.checkpoint > 1049 && questWorldData.checkpoint < 1099){
            questWorldData.checkpoint = 1111;
            RewriteQuestData.rewrite(listQuestWorldData, questWorldData);
        }

//        for(QuestWorldData questWorldData : list.allQuestWorlds){
//            if(questWorldData.isBusy && questWorldData.playerName.equalsIgnoreCase(e.getPlayer().getName())
//                    && e.getPlayer().getWorld().getName().equals(questWorldData.questWorldName)){
//
//                new RewriteDataInCycle().rewrite(list.allQuestWorlds.indexOf(questWorldData), questWorldData, editor, true);
//            }
//        }
    }
}
