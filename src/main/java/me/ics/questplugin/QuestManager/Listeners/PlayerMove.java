package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestStats;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesTxt.ListTxtWarpData;
import me.ics.questplugin.CustomClasses.ClassesTxt.TxtWarpData;
import me.ics.questplugin.CustomClasses.Statistic.ArrayProcessor;
import me.ics.questplugin.CustomClasses.Statistic.ListAllStatsData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteQuestData;
import me.ics.questplugin.HelpClasses.PlayerChecker;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class PlayerMove implements Listener {
    private FileJsonEditor<ListQuestWorldData> editorQuest;
    private FileJsonEditor<ListTxtWarpData> editorTxt;
    private FileJsonEditor<ListAllStatsData> editorStats;
    private Map<String, Boolean> places = new HashMap<>();

    public PlayerMove(Plugin plugin, String fileNameQuest, String fileNameTxt) {
        editorQuest = new FileJsonEditor<>(fileNameQuest, new ListQuestWorldData(), plugin);
        editorTxt = new FileJsonEditor<>(fileNameTxt, new ListTxtWarpData(), plugin);
        editorStats = new FileJsonEditor<>("/stats.txt", new ListAllStatsData(), plugin);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player quest_player = event.getPlayer();

        Location loc = quest_player.getLocation();
        ListTxtWarpData listTxtWarpData = editorTxt.getData();

//        if (checkRoadCross(quest_player, loc)) return;

        if (PlayerChecker.isNotInQuest(quest_player)) return;

        List<String> names = Arrays.asList("Sundau", "gigitog", "Leshachok");

        QuestWorldData questWorldData = editorQuest.getData().getQWDbyPlayer(quest_player.getName());
        if (questWorldData == null) return;

        mirror(quest_player);
        //сброс счетчика после задания с поиском пути

        if(questWorldData.counter != 0 && questWorldData.checkpoint == 301){
            questWorldData.counter = 0;
            RewriteQuestData.rewrite(editorQuest, questWorldData);
            return;
        }


//        if(!names.contains(quest_player.getName())) {
//            if(loc.getBlockZ() < 40 || loc.getBlockZ() > 590){
//                quest_player.performCommand("spawn");
//                quest_player.sendMessage(ChatColor.RED + "Пересёк границу мира!");
//            }
//            if (loc.getBlockY() < 10 || loc.getBlockY() > 224){
//                quest_player.performCommand("spawn");
//                quest_player.sendMessage(ChatColor.RED + "Пересёк границу мира!");
//            }
//            if(loc.getBlockX() > 1250 || loc.getBlockX() < 160){
//                quest_player.performCommand("spawn");
//                quest_player.sendMessage(ChatColor.RED + "Пересёк границу мира!");
//            }
//        }

        //find checkpointsQ

        if (questWorldData.checkpoint == 202) {
            Location loc2 = loc.add(0, -1, 0);
            if (loc.getBlock().getType().equals(Material.RED_WOOL)) {
                loc.getBlock().setType(Material.LIME_WOOL);
            } else if (loc2.getBlock().getType().equals(Material.RED_WOOL)) {
                loc.getBlock().setType(Material.LIME_WOOL);
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

                    if (txtWarp.name.equals("finalparkour") && !questWorldData.num_quests_complete.contains(701) ){
                        questWorldData.num_quests_complete.add(701);
                    }

                    if (txtWarp.name.equals("finalpve")  && !questWorldData.num_quests_complete.contains(702)){
                        questWorldData.num_quests_complete.add(702);
                    }

                    // перезапись в файле

                }
            } else {
                if (placesContain(name_place)) {
                    //if coords are incorrect, else statement is working. So we need
                    //to set "name"_"place" to "false" in the HashMap
                    remove(name_place);
                }
            }
        }
        boolean finish = false;
        //finish quest
        if (questWorldData.checkpoint == 1111 && !questWorldData.num_quests_complete.contains(1111)) {
            finish = true;
            // чтобы не входило больше
            questWorldData.num_quests_complete.add(1111);
            quest_player.sendMessage(ChatColor.DARK_AQUA + "Вы прошли квест за " + (quest_player.getTicksLived() + questWorldData.ticksSavedBeforeLeaving - questWorldData.ticksLivedWhenStart) / 20 + ChatColor.DARK_AQUA + " секунд! ");
            questWorldData.ticksPlayedFinal = (quest_player.getTicksLived() + questWorldData.ticksSavedBeforeLeaving - questWorldData.ticksLivedWhenStart);
            //телепорт
            quest_player.performCommand("spawn");
            Bukkit.unloadWorld(questWorldData.questWorldName,false);
            quest_player.getInventory().setItem(7, null);

            RewriteQuestData.rewrite(editorQuest, questWorldData);

            new ArrayProcessor(editorStats, questWorldData.votes, quest_player.getName()).writeStats();
            QuestStats questBook = new QuestStats(editorQuest, quest_player.getName(), editorStats, questWorldData.votes);
            quest_player.getInventory().setItem(4, questBook.makeBook());
            String command = "give " + quest_player.getName() + " written_book 1 {pages:[\"[\\\"\\\",{\\\"text\\\":\\\"Link\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"open_url\\\",\\\"value\\\":\\\"http://ac.opu.ua/\\\"},\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":[\\\"\\\",{\\\"text\\\":\\\"Site\\\",\\\"underlined\\\":true,\\\"color\\\":\\\"gray\\\"}]}},{\\\"text\\\":\\\"\\\\n\\\"}]\"], title:\"Custom Book\", author:Player}";

            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
        }
        // перезапись
        if(!finish) RewriteQuestData.rewrite(editorQuest, questWorldData);
    }

    private boolean checkRoadCross(Player quest_player, Location loc) {
        if (loc.getBlockY() == 66) {
            // переход дороги. Если наступает на дорогу, то он нарушитель
            if (loc.getBlockX() <= 283 && loc.getBlockX() >= 278) {
                loc.setX(277);
                quest_player.teleport(loc);
                quest_player.sendMessage(ChatColor.RED + "Нарушитель!");
                return true;
            }
            if (loc.getBlockX() <= 296 && loc.getBlockX() >= 291) {
                loc.setX(297);
                quest_player.teleport(loc);
                quest_player.sendMessage(ChatColor.RED + "Нарушитель!");
                return true;
            }
        }
        return false;
    }

    private void graphChecker(Player quest_player, ListTxtWarpData listTxtWarpData, QuestWorldData questWorldData, TxtWarpData txtWarp) {
        if (questWorldData.checkpoint == 202 && txtWarp.index > 2020
                && !questWorldData.num_quests_complete.contains(202)) {

            TxtWarpData txtWarpLevel = Objects.requireNonNull(listTxtWarpData.getWarpByCheck(202));
            boolean wasDeleted = false;
            //check if player was here
            if (questWorldData.num_quests_complete.contains(txtWarp.index)) {
                quest_player.sendMessage(ChatColor.RED + "Упс, ты уже тут был!");
                //обновление графа с зеленого на красный
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
                        questWorldData.num_quests_complete.remove(
                                questWorldData.num_quests_complete.indexOf(2020 + i));
                        wasDeleted = true;
                    }
                }
            }
            if (!wasDeleted)
                questWorldData.num_quests_complete.add(txtWarp.index);
            //check if player completed the graph
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

    private void mirror(Player player) {
        Location location = player.getLocation();
        Villager villager = null;
        if (location.getWorld().getName().startsWith("quest")) {
            if (location.getBlockX() >= 522 && location.getBlockX() <= 529 && location.getBlockZ() >= 709 && location.getBlockZ() <= 715) {
                if (location.getBlockX() >= 522 && location.getBlockX() <= 528 && location.getBlockZ() >= 709 && location.getBlockZ() <= 715) {
                    Location villagerLocation = location;
                    villagerLocation.setYaw(180 - location.getYaw());
                    villagerLocation.setZ(2*716.5 - location.getZ());
                    for (Entity entity : player.getNearbyEntities(9, 2, 15)) {
                        if (entity.getCustomName().equalsIgnoreCase(player.getName())) {
                            villager = (Villager) entity;
                            villager.setCustomName(player.getName());
                            villager.setCustomNameVisible(true);
                            break;
                        }
                    }
                    if (villager == null) {
                        villager = (Villager) location.getWorld().spawnEntity(villagerLocation, EntityType.VILLAGER);
                        villager.setAI(false);
                        villager.setCustomName(player.getName());
                        villager.setCustomNameVisible(true);
                    }
                    villager.teleport(villagerLocation);
                } else if (location.getBlockX() == 529 && location.getBlockZ() == 710) {
                    for (Entity entity : player.getNearbyEntities(9, 2, 15)) {
                        if (entity.getCustomName().equalsIgnoreCase(player.getName())) {
                            villager = (Villager) entity;
                            villager.setCustomNameVisible(false);
                            return;
                        }
                    }
                }
            }
        }
    }
}
