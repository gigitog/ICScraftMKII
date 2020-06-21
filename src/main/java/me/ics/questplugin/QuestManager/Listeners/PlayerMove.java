package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestStats;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesTxt.ListTxtWarpData;
import me.ics.questplugin.CustomClasses.ClassesTxt.TxtWarpData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class PlayerMove implements Listener {
    private FileJsonEditor<ListQuestWorldData> editorQuest;
    private FileJsonEditor<ListTxtWarpData> editorTxt;
    private Map<String, Boolean> places = new HashMap<>();

    public PlayerMove(Plugin plugin, String fileNameQuest, String fileNameTxt) {
        editorQuest = new FileJsonEditor<>(fileNameQuest, new ListQuestWorldData(), plugin);
        editorTxt = new FileJsonEditor<>(fileNameTxt, new ListTxtWarpData(), plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player quest_player = event.getPlayer();
        Location loc = quest_player.getLocation();
        ListQuestWorldData listQuestWorldData = editorQuest.getData();
        ListTxtWarpData listTxtWarpData = editorTxt.getData();
        boolean check = false;
        int indexOfQuestWorld = 0;
        QuestWorldData tempQuestData = new QuestWorldData(quest_player.getWorld());

        //find checkpoints
        for (QuestWorldData questWorldData : listQuestWorldData.allQuestWorlds) {
            // если мир занят игроком
            if (questWorldData.isBusy && questWorldData.playerName.equalsIgnoreCase(quest_player.getName())) {
                if (questWorldData.checkpoint == 202) {
                    Location loc2 = loc.add(0, -1, 0);
                    if (loc.getBlock().getType().equals(Material.RED_WOOL)) {
                        loc.getBlock().setType(Material.LIME_WOOL);
                    } else if (loc2.getBlock().getType().equals(Material.RED_WOOL)) {
                        loc.getBlock().setType(Material.LIME_WOOL);
                    }
                }
                if (loc.getBlockY() == 66) {
                    // переход дороги. Если наступает на дорогу, то он нарушитель
                    if (loc.getBlockX() <= 283 && loc.getBlockX() >= 278) {
                        loc.setX(277);
                        quest_player.teleport(loc);
                        quest_player.sendMessage(ChatColor.RED + "Нарушитель!");
                    }
                    if (loc.getBlockX() <= 296 && loc.getBlockX() >= 291) {
                        loc.setX(297);
                        quest_player.teleport(loc);
                        quest_player.sendMessage(ChatColor.RED + "Нарушитель!");
                    }
                }
                // проверка: этот квест (точка) уже была у игрока?
                // если нет, то мы добавим ее к списку его квестов.
                for (TxtWarpData txtWarp : listTxtWarpData.allData) {
                    boolean x = Math.abs(loc.getBlockX() - txtWarp.x) <= txtWarp.radius;
                    boolean y = Math.abs(loc.getBlockY() - txtWarp.y) <= 1;
                    boolean z = Math.abs(loc.getBlockZ() - txtWarp.z) <= txtWarp.radius;
                    boolean is = questWorldData.checkpoint < txtWarp.index;

                    String name_place = quest_player.getName().concat("_".concat(txtWarp.name));
                    if (x && y && z && is) {
                        if (!placesContain(name_place)) {
                            //if it's player's first step in the region, make name_place in map true
                            add(name_place);
                            // нужно проиграть звук
                            quest_player.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 100, 100);
                            // установка чекпоинта
                            if (txtWarp.index < 1500)
                                questWorldData.checkpoint = txtWarp.index;
                            //graph checker
                            graphChecker(quest_player, listTxtWarpData, questWorldData, txtWarp);
                            // перезапись в файле
                            check = true;
                            indexOfQuestWorld = listQuestWorldData.allQuestWorlds.indexOf(questWorldData);
                            tempQuestData = questWorldData;
                        }
                    } else {
                        if (placesContain(name_place)) {
                            //if coords are incorrect, else statement is working. So we need
                            //to set "name"_"place" to "false" in the HashMap
                            remove(name_place);
                        }
                    }
                }
                //finish quest
                if (questWorldData.checkpoint == 1000 && !questWorldData.num_quests_complete.contains(1000)) {
                    // чтобы не входило больше
                    questWorldData.num_quests_complete.add(1000);
                    quest_player.sendMessage(ChatColor.DARK_AQUA + "Вы прошли квест за " + (quest_player.getTicksLived() + questWorldData.ticksSavedBeforeLeaving - questWorldData.ticksLivedWhenStart) / 20 + ChatColor.DARK_AQUA + " секунд! ");
                    questWorldData.ticksPlayedFinal = (quest_player.getTicksLived() + questWorldData.ticksSavedBeforeLeaving - questWorldData.ticksLivedWhenStart);
                    //телепорт
                    quest_player.performCommand("spawn");

                    QuestStats questBook = new QuestStats(editorQuest, quest_player.getName());
                    quest_player.getInventory().setItem(4, questBook.makeBook());
                    quest_player.getInventory().setItem(7, null);
                    check = true;
                    indexOfQuestWorld = listQuestWorldData.allQuestWorlds.indexOf(questWorldData);
                    tempQuestData = questWorldData;
                }
                // перезапись
                new RewriteDataInCycle().rewrite(indexOfQuestWorld, tempQuestData, editorQuest, check);
            }
        }
    }

    private void graphChecker(Player quest_player, ListTxtWarpData listTxtWarpData, QuestWorldData questWorldData, TxtWarpData txtWarp) {
        if (questWorldData.checkpoint == 202 && txtWarp.index > 2020
                && !questWorldData.num_quests_complete.contains(202)) {

            TxtWarpData txtWarpLevel = Objects.requireNonNull(listTxtWarpData.getWarpByCheck(202));
            boolean wasDeleted = false;
            //check if player was here
            if (questWorldData.num_quests_complete.contains(txtWarp.index)) {
                quest_player.sendMessage(ChatColor.RED + "Упс, ты уже тут был!");
                for (int x = 651; x < 668; x++) {
                    for (int y = 72; y < 78; y++) {
                        for (int z = 455; z < 467; z++) {
                            Location wool = new Location(quest_player.getWorld(), x, y, z);
                            if (wool.getBlock().getType().equals(Material.LIME_WOOL)) {
                                wool.getBlock().setType(Material.RED_WOOL);
                            }
                        }
                    }
                }
                Location locTp = new Location(quest_player.getWorld(), txtWarpLevel.x, txtWarpLevel.y, txtWarpLevel.z);
                quest_player.teleport(locTp);
                //remove old checkpoints
                for (int i = 1; i < 9; i++) {
                    if (questWorldData.num_quests_complete.contains(2020 + i)) {
                        quest_player.sendMessage("rem " + 2020 + i);
                        questWorldData.num_quests_complete.remove(
                                questWorldData.num_quests_complete.indexOf(2020 + i)
                        );
                        wasDeleted = true;
                    }
                }
            }
            if (!wasDeleted)
                questWorldData.num_quests_complete.add(txtWarp.index);
            //check if player completed the
            int count = 0;
            for (int i = 1; i < 9; i++)
                if (questWorldData.num_quests_complete.contains(2020 + i))
                    count++;
            if (count == 8) {
                questWorldData.num_quests_complete.add(202);
                quest_player.playSound(quest_player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 30, 30);
                quest_player.sendMessage(ChatColor.GREEN + "Ты справился!");
                for (int i = 1; i < 9; i++) {
                    if (questWorldData.num_quests_complete.contains(2020 + i)) {
                        questWorldData.num_quests_complete.remove(
                                questWorldData.num_quests_complete.indexOf(2020 + i)
                        );
                    }
                }
            }
        }
    }

    private boolean placesContain(String name_place) {
        if (places.get(name_place) == null)
            return false;
        return places.get(name_place);
    }

    // add playerName_placeName to map and make it True
    private void add(String name_place) {
        places.put(name_place, true);
    }

    // playerName_placeName set false
    private void remove(String name_place) {
        places.put(name_place, false);
    }
}
