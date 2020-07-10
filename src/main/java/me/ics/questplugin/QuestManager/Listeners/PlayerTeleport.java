package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
import me.ics.questplugin.FileEditor.RewriteQuestData;
import me.ics.questplugin.HelpClasses.QuestInstruments;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class PlayerTeleport implements Listener {
    private FileJsonEditor<ListQuestWorldData> editor;

    public PlayerTeleport(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event){
        Location from = event.getFrom();
        Location to = event.getTo();
        Player player = event.getPlayer();
        QuestWorldData questWorldData = editor.getData().getQWDbyPlayer(player.getName());
        if(questWorldData == null) return;

        if(!from.getWorld().equals(to.getWorld()) && from.getWorld().getName().startsWith("quest")){
            if(questWorldData.ticksPlayedFinal!=0) return;
            player.getInventory().setItem(4, new QuestInstruments().makeQuestBook());
            questWorldData.ticksSavedBeforeLeaving += player.getTicksLived()-questWorldData.ticksLivedWhenStart;
            questWorldData.ticksLivedWhenStart = 0;
            questWorldData.spawn = new double[]{from.getX(),from.getY(),from.getZ()};
            RewriteQuestData.rewrite(editor,questWorldData);
            return;
        }
        if(!from.getWorld().equals(to.getWorld()) && to.getWorld().getName().startsWith("quest")){
            player.getInventory().setItem(4, new QuestInstruments().makeEndRedstone());
            questWorldData.ticksLivedWhenStart = player.getTicksLived();
            RewriteQuestData.rewrite(editor,questWorldData);
        }
    }
}
