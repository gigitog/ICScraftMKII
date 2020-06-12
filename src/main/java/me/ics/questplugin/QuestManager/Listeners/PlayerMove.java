package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.SideQuestOne;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

public class PlayerMove implements Listener {
    private FileJsonEditor<ListQuestWorldData> editor;

    public PlayerMove(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        Player quest_player = event.getPlayer();
        Location loc = quest_player.getLocation();
        ListQuestWorldData listQuestWorldData = editor.getData();
        boolean check = false;
        int indexOfQuestWorld = 0;
        QuestWorldData tempQuestData = new QuestWorldData(quest_player.getWorld());
        for(QuestWorldData questWorldData : listQuestWorldData.allQuestWorlds){
            // если мир занят игроком
            if(questWorldData.isBusy && questWorldData.playerName.equalsIgnoreCase(quest_player.getName())){
                Integer checkpoint = questWorldData.checkpoint;
                if(loc.getBlockY() == 66){
                    // переход дороги. Если наступает на дорогу, то он нарушитель
                    if(loc.getBlockX() <= 283 && loc.getBlockX() >= 278){
                        loc.setX(277.8);
                        quest_player.teleport(loc);
                        quest_player.sendMessage(ChatColor.RED + "Нарушитель!");
                    }
                    if(loc.getBlockX() <= 296 && loc.getBlockX() >= 291){
                        loc.setX(297.2);
                        quest_player.teleport(loc);
                        quest_player.sendMessage(ChatColor.RED + "Нарушитель!");
                    }
                }

                if(checkpoint == 1 && (Math.abs((int) loc.getX() - 271) < 2) && (int) loc.getY() == 61 && (Math.abs((int) loc.getZ() - 242) < 2)){
                    tempQuestData = questWorldData;
                    indexOfQuestWorld = listQuestWorldData.allQuestWorlds.indexOf(questWorldData);
                    check = true;
                    quest_player.sendMessage("Баянист : Помоги...");
                    questWorldData.checkpoint = 3;
                }
                if(checkpoint == 10){
                    check = true;
                    indexOfQuestWorld = listQuestWorldData.allQuestWorlds.indexOf(questWorldData);
                    tempQuestData = questWorldData;
                    quest_player.sendMessage(ChatColor.DARK_AQUA + "Вы прошли квест за " + (quest_player.getTicksLived() + questWorldData.ticksSavedBeforeLeaving - questWorldData.ticksLivedWhenStart)/20 + ChatColor.DARK_AQUA + " секунд! ");
                    questWorldData.ticksPlayedFinal = (quest_player.getTicksLived() + questWorldData.ticksSavedBeforeLeaving - questWorldData.ticksLivedWhenStart);
                    quest_player.performCommand("spawn");
                    questWorldData.checkpoint = 11;
                }
                new RewriteDataInCycle().rewrite(indexOfQuestWorld, tempQuestData, editor, check);
            }
        }

    }
}
