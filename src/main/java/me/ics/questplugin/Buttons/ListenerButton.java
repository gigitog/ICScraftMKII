package me.ics.questplugin.Buttons;

import me.ics.questplugin.CustomClasses.ClassesButton.ButtonData;
import me.ics.questplugin.CustomClasses.ClassesButton.ListButtonData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.Objects;

public class ListenerButton implements Listener {
    private FileJsonEditor<ListButtonData> editorButton;
    private FileJsonEditor<ListQuestWorldData> editorQuest;

    public ListenerButton(Plugin plugin, String fileButton, String fileQuest) {
        editorButton = new FileJsonEditor<>(fileButton, new ListButtonData(), plugin);
        editorQuest = new FileJsonEditor<>(fileQuest, new ListQuestWorldData(), plugin);
    }
    @EventHandler
    public void onButton(PlayerInteractEvent event){
        if(!event.hasBlock()) return;
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) &&
                Objects.requireNonNull(event.getClickedBlock()).
                        getType().equals(Material.STONE_BUTTON)){

            Player player = event.getPlayer();
            Location loc = event.getClickedBlock().getLocation();
            Location locTp = new Location(event.getPlayer().getWorld(),0,0,0, 0, 0);

            //list
            ListButtonData buttons = editorButton.getData();
            ListQuestWorldData listQuestWorlds  = editorQuest.getData();
            // search
            for(ButtonData button : buttons.allData){
                // condition that player clicked on button in file
                boolean isHere = loc.getBlockX() == button.x &&
                        loc.getBlockY() == button.y && loc.getBlockZ() == button.z;
                if(isHere){
                    boolean canTp = true;
                    // добавляем в индекс массива оценок голос игрока по поводу данной секции
                    for (QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds) {
                        if (questWorldData.playerName.equalsIgnoreCase(player.getName())) {
                            if(button.checkpoint > questWorldData.checkpoint){
                                player.sendMessage(ChatColor.RED + "Туда еще рано!");
                                canTp = false;
                                break;
                            }
                            if(button.index != -1 && button.value != -1){
                                questWorldData.votes[button.index] = button.value;
                                new RewriteDataInCycle().rewrite(listQuestWorlds.allQuestWorlds.indexOf(questWorldData),
                                        questWorldData, editorQuest, true);
                            }
                        }
                    }

                    // set Location, Yaw and Pitch then tp
                    locTp.add(button.xtp + 0.5, button.ytp, button.ztp + 0.5);
                    locTp.setYaw(loc.getYaw());
                    locTp.setPitch(loc.getPitch());
                    if(canTp)
                    player.teleport(locTp);
                }
            }
        }
    }
}
