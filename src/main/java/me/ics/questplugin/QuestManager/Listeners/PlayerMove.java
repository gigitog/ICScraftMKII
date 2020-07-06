package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestStats;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesTxt.ListTxtWarpData;
import me.ics.questplugin.CustomClasses.ClassesTxt.TxtWarpData;
import me.ics.questplugin.CustomClasses.Statistic.ArrayProcessor;
import me.ics.questplugin.CustomClasses.Statistic.ListAllStatsData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
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
        mirror(quest_player);
        Location loc = quest_player.getLocation();
        ListQuestWorldData listQuestWorldData = editorQuest.getData();
        ListTxtWarpData listTxtWarpData = editorTxt.getData();
        boolean check = false;
        int indexOfQuestWorld = 0;
        QuestWorldData tempQuestData = null;

        if (loc.getBlockY() == 66) {
            // переход дороги. Если наступает на дорогу, то он нарушитель
            if (loc.getBlockX() <= 283 && loc.getBlockX() >= 278) {
                loc.setX(277);
                quest_player.teleport(loc);
                quest_player.sendMessage(ChatColor.RED + "Нарушитель!");
                return;
            }
            if (loc.getBlockX() <= 296 && loc.getBlockX() >= 291) {
                loc.setX(297);
                quest_player.teleport(loc);
                quest_player.sendMessage(ChatColor.RED + "Нарушитель!");
                return;
            }
        }
        List<String> names = new ArrayList<>();
        names.add("Sundau");
        names.add("gigitog");
        names.add("Leshachok");
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

        //find checkpoints
        for (QuestWorldData questWorldData : listQuestWorldData.allQuestWorlds) {
            // если мир занят игроком
            if (questWorldData.isBusy && questWorldData.playerName.equalsIgnoreCase(quest_player.getName())){
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
                boolean finish = false;
                //finish quest
                if (questWorldData.checkpoint == 1000 && !questWorldData.num_quests_complete.contains(1000)) {
                    finish = true;
                    // чтобы не входило больше
                    questWorldData.num_quests_complete.add(1000);
                    quest_player.sendMessage(ChatColor.DARK_AQUA + "Вы прошли квест за " + (quest_player.getTicksLived() + questWorldData.ticksSavedBeforeLeaving - questWorldData.ticksLivedWhenStart) / 20 + ChatColor.DARK_AQUA + " секунд! ");
                    questWorldData.ticksPlayedFinal = (quest_player.getTicksLived() + questWorldData.ticksSavedBeforeLeaving - questWorldData.ticksLivedWhenStart);
                    //телепорт
                    quest_player.performCommand("spawn");
                    quest_player.getInventory().setItem(7, null);
                    check = true;
                    indexOfQuestWorld = listQuestWorldData.allQuestWorlds.indexOf(questWorldData);
                    tempQuestData = questWorldData;
                    new RewriteDataInCycle().rewrite(indexOfQuestWorld, tempQuestData, editorQuest, check);
                    new ArrayProcessor(editorStats, questWorldData.votes, quest_player.getName()).writeStats();
                    QuestStats questBook = new QuestStats(editorQuest, quest_player.getName(), editorStats, questWorldData.votes);
                    quest_player.getInventory().setItem(4, questBook.makeBook());
                    Bukkit.getConsoleSender().sendRawMessage
                            ("give " + quest_player.getName() + " written_book 1 {pages:[\"[\\\"\\\",{\\\"text\\\":\\\"Link\\\",\\\"clickEvent\\\":{\\\"action\\\":\\\"open_url\\\",\\\"value\\\":\\\"http://ac.opu.ua/\\\"},\\\"hoverEvent\\\":{\\\"action\\\":\\\"show_text\\\",\\\"value\\\":[\\\"\\\",{\\\"text\\\":\\\"Site\\\",\\\"underlined\\\":true,\\\"color\\\":\\\"gray\\\"}]}},{\\\"text\\\":\\\"\\\\n\\\"}]\"], title:\"Custom Book\", author:Player}");
                }
                // перезапись
                if(!finish) new RewriteDataInCycle().rewrite(indexOfQuestWorld, tempQuestData, editorQuest, check);
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

    private void mirror(Player player){
        Location location = player.getLocation();
        Villager villager = null;
        if(location.getWorld().getName().startsWith("quest")){
            if(location.getBlockX()>=602 && location.getBlockX()<=609 && location.getBlockZ()>=539 && location.getBlockZ()<=546){
                if(location.getBlockX()>=602 && location.getBlockX()<=608 && location.getBlockZ()>=539 && location.getBlockZ()<=546){
                    Location villagerLocation = location;
                    villagerLocation.setYaw(180-location.getYaw());
                    villagerLocation.setZ(546.5-location.getZ()+546.5);
                    for(Entity entity : player.getNearbyEntities(9,2,15)){
                        if(entity.getCustomName().equalsIgnoreCase(player.getName())){
                            villager = (Villager)entity;
                            villager.setCustomName(player.getName());
                            villager.setCustomNameVisible(true);
                            break;
                        }
                    }
                    if(villager == null) {
                        villager = (Villager)location.getWorld().spawnEntity(villagerLocation, EntityType.VILLAGER);
                        villager.setAI(false);
                        villager.setCustomName(player.getName());
                        villager.setCustomNameVisible(true);
                    }
                    villager.teleport(villagerLocation);
                }
                else if(location.getBlockX()==609 && location.getBlockZ()==540){
                    for(Entity entity : player.getNearbyEntities(9,2,15)){
                        if(entity.getCustomName().equalsIgnoreCase(player.getName())){
                            villager = (Villager)entity;
                            villager.setCustomNameVisible(false);
                            return;
                        }
                    }
                }
            }
        }
    }
}
