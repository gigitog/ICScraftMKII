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
import me.ics.questplugin.HelpClasses.WorldRecreator;
import org.bukkit.*;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class PlayerMove implements Listener {
    private FileJsonEditor<ListQuestWorldData> editorQuest;
    private FileJsonEditor<ListTxtWarpData> editorTxt;
    private FileJsonEditor<ListAllStatsData> editorStats;
    private Map<String, Boolean> places = new HashMap<>();
    private ListTxtWarpData listTxtWarpData;
    private ListQuestWorldData listQuestWorldData;

    public PlayerMove(Plugin plugin, String fileNameQuest, String fileNameTxt, ListQuestWorldData listQuestWorldData) {
        editorQuest = new FileJsonEditor<>(fileNameQuest, new ListQuestWorldData(), plugin);
        editorTxt = new FileJsonEditor<>(fileNameTxt, new ListTxtWarpData(), plugin);
        editorStats = new FileJsonEditor<>("/stats.txt", new ListAllStatsData(), plugin);
        listTxtWarpData = editorTxt.getData();
        this.listQuestWorldData = listQuestWorldData;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player quest_player = event.getPlayer();

        Location loc = quest_player.getLocation();

        if (PlayerChecker.isNotInQuest(quest_player)) return;

        QuestWorldData questWorldData = listQuestWorldData.getQWDbyPlayer(quest_player.getName());
        if (questWorldData == null) return;
//        YuraScheck(questWorldData);
        mirror(quest_player);
        //сброс счетчика после задания с поиском пути

        if(questWorldData.counter != 0 && questWorldData.checkpoint == 301){
            questWorldData.counter = 0;
            RewriteQuestData.rewrite(listQuestWorldData, questWorldData);
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

        if (questWorldData.checkpoint == 201) {
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
            boolean is = questWorldData.checkpoint <= txtWarp.index;

            String name_place = quest_player.getName().concat("_".concat(txtWarp.name));
            if (x && y && z && is) {
                if (!placesContain(name_place)) {
                    //if it's player's first step in the region, make name_place in map true
                    add(name_place);
                    // нужно проиграть звук
                    if (questWorldData.checkpoint < txtWarp.index){
                        quest_player.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 100, 100);
                    }


                    // установка чекпоинта
                    if (txtWarp.index < 1500)
                        questWorldData.checkpoint = txtWarp.index;
                    //graph checker
                    graphChecker(quest_player, listTxtWarpData, questWorldData, txtWarp);

                    if (txtWarp.x == 992 && !questWorldData.num_quests_complete.contains(701) ){
                        questWorldData.num_quests_complete.add(701);
                    }

                    if (txtWarp.index == 1050){
                        ItemStack secretBook = new QuestStats(editorQuest, quest_player, editorStats, listQuestWorldData).makeBookSecret();
                        Location locChest = new Location(quest_player.getWorld(), 532, 66, 666);
                        if (quest_player.getWorld().getName().startsWith("quest")){
                            Chest chest = (Chest) locChest.getBlock().getState();
                            if (chest.getInventory().getItem(0) != null) return;
                            chest.getBlockInventory().addItem(secretBook);
                        }
                    }

                    RewriteQuestData.rewrite(listQuestWorldData, questWorldData);

                    break;
                }
            } else {
                if (placesContain(name_place)) {
                    //if coords are incorrect, else statement is working. So we need
                    //to set "name"_"place" to "false" in the HashMap
                    remove(name_place);
                    break;
                }
            }
        }

        boolean finish = false;
        //finish quest
        finish = isFinish(quest_player, questWorldData, finish);
    }

    private boolean isFinish(Player quest_player, QuestWorldData questWorldData, boolean finish) {
        if (questWorldData.checkpoint == 1111 && !questWorldData.num_quests_complete.contains(1111)) {
            finish = true;
            // чтобы не входило больше
            questWorldData.num_quests_complete.add(1111);
            quest_player.sendMessage(ChatColor.DARK_AQUA + "Вы прошли квест за " + (quest_player.getTicksLived() + questWorldData.ticksSavedBeforeLeaving - questWorldData.ticksLivedWhenStart) / 20 + ChatColor.DARK_AQUA + " секунд! ");
            questWorldData.ticksPlayedFinal = (quest_player.getTicksLived() + questWorldData.ticksSavedBeforeLeaving - questWorldData.ticksLivedWhenStart);
            //телепорт
            quest_player.performCommand("spawn");
////            Bukkit.unloadWorld(questWorldData.questWorldName, false);
//            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv delete " + questWorldData.questWorldName);
//            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mvconfirm");
//            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "quest create 1");

            new WorldRecreator(Bukkit.getWorld(questWorldData.questWorldName)).worldRecreator();

            QuestWorldData qwd = new QuestWorldData(Bukkit.getWorld(questWorldData.questWorldName));
            listQuestWorldData.allQuestWorlds.add(qwd);
            editorQuest.setData(listQuestWorldData);

            if (quest_player.getInventory().getItem(7) != null) {
                Objects.requireNonNull(quest_player.getInventory().getItem(7)).setAmount(0);
            }

            RewriteQuestData.rewrite(listQuestWorldData, questWorldData);

            new ArrayProcessor(editorStats, questWorldData.votes, quest_player.getName()).writeStats();
            QuestStats questBook = new QuestStats(editorQuest, quest_player, editorStats, listQuestWorldData);
            quest_player.getInventory().setItem(4, questBook.makeBook());

        }
        return finish;
    }

    private void graphChecker(Player quest_player, ListTxtWarpData listTxtWarpData, QuestWorldData questWorldData, TxtWarpData txtWarp) {
        if (questWorldData.checkpoint == 201 && txtWarp.index > 2010
                && !questWorldData.num_quests_complete.contains(201)) {

            TxtWarpData txtWarpLevel = Objects.requireNonNull(listTxtWarpData.getWarpByCheck(201));
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
                    if (questWorldData.num_quests_complete.contains(2010 + i)) {
                        questWorldData.num_quests_complete.remove(
                                questWorldData.num_quests_complete.indexOf(2010 + i));
                        wasDeleted = true;
                    }
                }
            }
            if (!wasDeleted)
                questWorldData.num_quests_complete.add(txtWarp.index);
            //check if player completed the graph
            int count = 0;
            for (int i = 1; i < 9; i++)
                if (questWorldData.num_quests_complete.contains(2010 + i))
                    count++;
            if (count == 8) {
                questWorldData.num_quests_complete.add(201);
                quest_player.playSound(quest_player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, 30, 30);
                quest_player.sendMessage(ChatColor.GREEN + "Ты справился!");
                for (int i = 1; i < 9; i++) {
                    if (questWorldData.num_quests_complete.contains(2010 + i)) {
                        questWorldData.num_quests_complete.remove(
                                questWorldData.num_quests_complete.indexOf(2010 + i)
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

//    private void YuraScheck(QuestWorldData questWorldData){
//        Location location = new Location(Bukkit.getWorld("world"),414 ,65,336);
//        if(questWorldData.counter == 666 && Objects.requireNonNull(Bukkit.getPlayer(questWorldData.playerName)).getLocation().equals(location)){
//            questWorldData.counter=0;
//            Objects.requireNonNull(Bukkit.getPlayer(questWorldData.playerName)).sendMessage("["+ChatColor.GREEN+"Дядя Юра"+ChatColor.RESET+"] Прошел квест! Красавчик!" );
//            RewriteQuestData.rewrite(editorQuest,questWorldData);
//        }
//    }
}
