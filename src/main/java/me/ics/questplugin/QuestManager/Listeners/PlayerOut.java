package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
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
        for(QuestWorldData questWorldData : list.allQuestWorlds){
            if(questWorldData.isBusy && questWorldData.playerName.equalsIgnoreCase(e.getPlayer().getName()) && e.getPlayer().getWorld().getName().equalsIgnoreCase(questWorldData.questWorldName)){
                questWorldData.ticksSavedBeforeLeaving += e.getPlayer().getTicksLived() - questWorldData.ticksLivedWhenStart;
                questWorldData.ticksLivedWhenStart = 0;
                new RewriteDataInCycle().rewrite(list.allQuestWorlds.indexOf(questWorldData), questWorldData, editor, true);
            }
        }

    }
}
