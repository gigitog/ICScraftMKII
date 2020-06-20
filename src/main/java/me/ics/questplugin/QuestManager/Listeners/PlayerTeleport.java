package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

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
        if(!from.getWorld().equals(to.getWorld()) && from.getWorld().getName().startsWith("quest")){
            int index = 0;
            for(QuestWorldData questWorldData : editor.getData().allQuestWorlds){
                if(questWorldData.ticksPlayedFinal==0 && player.getName().equalsIgnoreCase(questWorldData.playerName)){
                    ItemStack book = new ItemStack(Material.BOOK);
                    ItemMeta meta = book.getItemMeta();
                    meta.setDisplayName("§aНачать квест §7(ПКМ)");
                    book.setItemMeta(meta);
                    book.setAmount(1);
                    player.getInventory().setItem(4,book);
                    questWorldData.ticksSavedBeforeLeaving += player.getTicksLived()-questWorldData.ticksLivedWhenStart;
                    questWorldData.ticksLivedWhenStart = 0;
                    questWorldData.spawn = new double[]{from.getX(),from.getY(),from.getZ()};
                    new RewriteDataInCycle().rewrite(index, questWorldData, editor,true);
                    return;
                }
                index++;
            }
        }
        if(!from.getWorld().equals(to.getWorld()) && to.getWorld().getName().startsWith("quest")){
            int index = 0;
            for(QuestWorldData questWorldData : editor.getData().allQuestWorlds){
                if(questWorldData.ticksPlayedFinal==0 && player.getName().equalsIgnoreCase(questWorldData.playerName)){
                    questWorldData.ticksLivedWhenStart = player.getTicksLived();
                    player.getInventory().setItem(4,null);
                    new RewriteDataInCycle().rewrite(index,questWorldData,editor,true);
                    return;
                }
                index++;
            }
        }
    }
}
