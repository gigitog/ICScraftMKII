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

    public PlayerOut(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
    }

    @EventHandler
    public void onPlayerExit(PlayerQuitEvent e){
        ListQuestWorldData list = editor.getData();
        QuestWorldData questWorldData = list.getQWDbyPlayer(e.getPlayer().getName());
        if(questWorldData!=null){
            if(questWorldData.questWorldName.equalsIgnoreCase(e.getPlayer().getWorld().getName())){
                questWorldData.ticksSavedBeforeLeaving += e.getPlayer().getTicksLived() - questWorldData.ticksLivedWhenStart;
                questWorldData.ticksLivedWhenStart = 0;
                RewriteQuestData.rewrite(editor,questWorldData);
            }
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
