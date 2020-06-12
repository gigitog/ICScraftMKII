package me.ics.questplugin.QuestManager.Listeners;

import com.google.gson.internal.$Gson$Preconditions;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.Plugin;

public class PlayerTeleport implements Listener {
    private FileJsonEditor<ListQuestWorldData> editor;

    public PlayerTeleport(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event){
        Location from = event.getFrom();
        Player player = event.getPlayer();
        for(QuestWorldData questWorldData : editor.getData().allQuestWorlds){
            if(from.getWorld().getName().equalsIgnoreCase(questWorldData.questWorldName) && questWorldData.ticksPlayedFinal==0){
                questWorldData.ticksSavedBeforeLeaving += player.getTicksLived() - questWorldData.ticksLivedWhenStart;
            }
        }
    }
}
