package me.ics.questplugin.QuestManager.Commands;

import com.sun.tools.javac.jvm.Items;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestStats;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.CustomClasses.Statistic.ListAllStatsData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class QuestOperator implements CommandExecutor {
    private FileJsonEditor<ListQuestWorldData> editor;
    private FileJsonEditor<ListAllStatsData> editorStats;

    public QuestOperator(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
        editorStats = new FileJsonEditor<>("/stats.txt", new ListAllStatsData(), plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        ListQuestWorldData listQuestWorlds = editor.getData();
        Player player = (Player) sender;
        int indexOfQuestWorld = 0;
        QuestWorldData tempQuestData = null;
        //    /quest
        if (rawQuestCommand(args, listQuestWorlds, player, indexOfQuestWorld, tempQuestData))
            return true;
        if (!player.isOp()) return true;
        //    /quest status
        if (status(args, listQuestWorlds, player)) return true;
        //    /quest create xxx
        if (createWorld(args, listQuestWorlds, player)) return true;
        //    /quest worldlist
        if (worldList(args, listQuestWorlds, player)) return true;
        //    /quest playerlist
        if (playerList(args, listQuestWorlds, player)) return true;
        //    /quest reload
        if (reload(args, listQuestWorlds, player)) return true;
        //    /quest remove xxx
        if (removeWorld(args, listQuestWorlds, player)) return true;
        return false;
    }
    //    /quest
    private boolean rawQuestCommand(String[] args, ListQuestWorldData listQuestWorlds, Player player, int indexOfQuestWorld, QuestWorldData tempQuestData) {
        if (args.length == 0) {
            if (listQuestWorlds.allQuestWorlds.size() != 0) {
                for (QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds) {
                    if (questWorldData.isBusy && questWorldData.playerName.equalsIgnoreCase(player.getName()) && questWorldData.ticksPlayedFinal == 0) {
                        if (player.getWorld().getName().equalsIgnoreCase(questWorldData.questWorldName)) {
                            player.sendMessage("Вы уже в квесте!");
                            return true;
                        }
                        Location saved = new Location(Bukkit.getWorld(questWorldData.questWorldName), questWorldData.spawn[0], questWorldData.spawn[1], questWorldData.spawn[2]);

                        player.getInventory().setItem(4, new ItemStack(Material.AIR));

                        player.sendMessage(ChatColor.GREEN + "Телепортация на сохраненную локацию.");
                        player.teleport(saved);
                        return true;
                    }
                    if (questWorldData.ticksPlayedFinal != 0 && questWorldData.playerName.equalsIgnoreCase(player.getName())) {
//                        int i = (int) (Math.random() * 10);
//                        switch (i) {
//                            case 1:
//                                break;
//                        }
                        player.sendMessage("Вы уже прошли квест");
                        return true;
                    }
                }
                for (QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds) {
                    if (!questWorldData.isBusy) {
                        indexOfQuestWorld = listQuestWorlds.allQuestWorlds.indexOf(questWorldData);
                        tempQuestData = questWorldData;
                        World quest = Bukkit.getWorld(questWorldData.questWorldName);
                        questWorldData.isBusy = true;
                        questWorldData.playerName = player.getName();
                        questWorldData.ticksLivedWhenStart = player.getTicksLived();
                        questWorldData.ticksSavedBeforeLeaving = 0;
                        Location loc_spawn = new Location(quest, questWorldData.spawn[0], questWorldData.spawn[1], questWorldData.spawn[2]);
                        assert quest != null;
                        quest.setTime(6000);
                        player.teleport(loc_spawn);
                        loc_spawn.setX(0);
                        loc_spawn.setY(1);
                        loc_spawn.setZ(0);
                        player.setGameMode(GameMode.ADVENTURE);
                        player.getInventory().setItem(4, new ItemStack(Material.AIR));
                        break;
                    }
                }
                if (tempQuestData != null) {
                    listQuestWorlds.allQuestWorlds.remove(indexOfQuestWorld);
                    listQuestWorlds.allQuestWorlds.add(indexOfQuestWorld, tempQuestData);
                    editor.setData(listQuestWorlds);
                    return true;
                }
            }
            player.sendMessage(ChatColor.RED + "Свободные миры для квеста закончились!");
            return true;
        }
        return false;
    }

    //    /quest status
    private boolean status(String[] args, ListQuestWorldData listQuestWorlds, Player player) {
        if (args.length == 2 && args[0].equalsIgnoreCase("status")) {
            String name = args[1];
            for (QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds) {
                // информация о мире и игроке
                if (questWorldData.isBusy && questWorldData.playerName.equalsIgnoreCase(name)) {

                    player.sendMessage(ChatColor.GREEN + "Квестовый мир - " + questWorldData.questWorldName);
                    player.sendMessage(ChatColor.GREEN + "Игрок - " + questWorldData.playerName);
                    player.sendMessage(ChatColor.GREEN + "Текущий квест - " + questWorldData.checkpoint);
                    player.sendMessage(ChatColor.GREEN + "Пройденные квесты - " + questWorldData.num_quests_complete.toString());
                    player.sendMessage(ChatColor.BLUE + "Оценки: " + Arrays.toString(questWorldData.votes));

                    ItemStack book = new QuestStats(editor, player.getName(), editorStats, questWorldData.votes).makeBook();
                    book.setAmount(1);
                    player.getInventory().addItem(book);

                    if (questWorldData.ticksPlayedFinal != 0) {
                        player.sendMessage(ChatColor.GREEN + "Квест пройден за - " + questWorldData.ticksPlayedFinal / 1200 + " мин., " + questWorldData.ticksPlayedFinal % 1200 / 20 + " сек.");
                        return true;
                    } else if (player.getWorld().getName().equalsIgnoreCase(questWorldData.questWorldName)) {
                        player.sendMessage(ChatColor.GREEN + "Проведено времени в квесте - " + (questWorldData.ticksSavedBeforeLeaving + (player.getTicksLived() - questWorldData.ticksLivedWhenStart)) / 1200 + " мин, " + (questWorldData.ticksSavedBeforeLeaving + player.getTicksLived() - questWorldData.ticksLivedWhenStart) % 1200 / 20 + " сек.");
                        return true;
                    }
                    player.sendMessage(ChatColor.GREEN + "Проведено времени в квесте - " + questWorldData.ticksSavedBeforeLeaving / 1200 + " мин, " + (questWorldData.ticksSavedBeforeLeaving % 1200) / 20 + " сек.");
                    return true;
                }
            }
            player.sendMessage(ChatColor.RED + "Игрок не находится/находился в квесте!");
            return true;
        }
        return false;
    }

    //    /quest create xxx
    private boolean createWorld(String[] args, ListQuestWorldData listQuestWorlds, Player player) {
        if (args.length == 2 && args[0].equalsIgnoreCase("create")) {
            try {
                Integer num = Integer.valueOf(args[1]);
                if (num < 1 || num > 5) {
                    player.sendMessage(ChatColor.RED + "Не стоит этого делать");
                    return true;
                }
                // создаем миры с разными названиями
                for (int i = 0; i < num; i++) {
                    String name;
                    for (int y = 1; ; y++) {
                        name = "quest";
                        name = name + y;
                        boolean was = false;
                        for (World w : Bukkit.getWorlds()) {
                            if (w.getName().equals(name)) {
                                was = true;
                            }
                        }
                        if (!was) {
                            // если такого мира нет
                            player.performCommand("mv clone worldquest " + name);
                            Thread.sleep(250);
                            QuestWorldData questWorldData = new QuestWorldData(Bukkit.getWorld(name));
                            listQuestWorlds.allQuestWorlds.add(questWorldData);
                            // запись в файл
                            editor.setData(listQuestWorlds);
                            break;
                        }
                    }
                }
            } catch (NumberFormatException | InterruptedException e) {
                player.sendMessage(ChatColor.RED + "Неправильное значение количества миров!");
            }
            return true;
        }
        return false;
    }

    private Function<QuestWorldData, String> passed = x -> {
        String name = "&a" + x.questWorldName;
        String s =  name + " - занят игроком " + ChatColor.YELLOW + x.playerName;
        if (x.isBusy){
            if (x.ticksPlayedFinal == 0) return s + "&a. Статус - &eпрохождение&a.";
            return s + "&a. Статус - &2пройден&a.";
        } else
            return name + ChatColor.AQUA + " - свободен ";
    };

    //    /quest worldlist
    private boolean worldList(String[] args, ListQuestWorldData listQuestWorlds, Player player) {
        // вывод всех миров
        if (args.length == 1 && args[0].equalsIgnoreCase("worldlist")) {
            if (listQuestWorlds.allQuestWorlds.size() == 0) {
                player.sendMessage(ChatColor.DARK_PURPLE + "Список миров для квеста пуст!");
                return true;
            }

            int counter = 0;
            for(QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds){
                if(questWorldData.isBusy) counter++;
            }
            player.sendMessage(ChatColor.GREEN + "Всего - " + listQuestWorlds.allQuestWorlds.size() +". Занято - "+ counter +".");

            for (QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds)
                player.sendMessage(color(passed.apply(questWorldData)));

            return true;
        }
        return false;
    }

    //    /quest playerlist
    private boolean playerList(String[] args, ListQuestWorldData listQuestWorlds, Player player) {
        if(args.length==1 && args[0].equalsIgnoreCase("playerlist")){
            if (listQuestWorlds.allQuestWorlds.size() == 0) {
                player.sendMessage(ChatColor.DARK_PURPLE + "Список миров для квеста пуст!");
                return true;
            }
            player.sendMessage(ChatColor.DARK_GREEN + "Список игроков: ");
            for(QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds){
                if(questWorldData.isBusy){
                    if(questWorldData.ticksPlayedFinal==0){
                        player.sendMessage(ChatColor.GREEN + "Игрок "+ questWorldData.playerName + " играет в мире " +questWorldData.questWorldName);
                    } else player.sendMessage(ChatColor.GREEN + "Игрок "+ questWorldData.playerName + " прошел квест в мире " +questWorldData.questWorldName);
                }
            }
            return true;
        }
        return false;
    }

    //    /quest remove xxx
    private boolean removeWorld(String[] args, ListQuestWorldData listQuestWorlds, Player player) {
        // удаление мира
        if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            String worldName = args[1];
            for (QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds) {
                if (questWorldData.questWorldName.equalsIgnoreCase(worldName)) {
                    //удаление и перезапись
                    listQuestWorlds.allQuestWorlds.remove(questWorldData);
                    editor.setData(listQuestWorlds);
                    player.sendMessage(ChatColor.GREEN + "Запись о квестe в мире " +
                            worldName + " успешно удалена!");
                    return true;
                }
            }
            player.sendMessage(ChatColor.RED + "Такого мира нет!");
            return true;
        }
        return false;
    }

    //    /quest reload
    private boolean reload(String[] args, ListQuestWorldData listQuestWorlds, Player player) {
        // перезагрузка
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            List<QuestWorldData> tempDataToDelete = new ArrayList<>();
            // удаляем лишние миры из массива
            // Находим нулевые миры в файле, записывем во временный массив
            for (QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds) {
                if (Bukkit.getWorld(questWorldData.questWorldName) == null) {
                    tempDataToDelete.add(questWorldData);
                }
            }
            for (QuestWorldData questWorldData : tempDataToDelete) {
                listQuestWorlds.allQuestWorlds.remove(questWorldData);
            }
            editor.setData(listQuestWorlds);

            player.sendMessage(ChatColor.GREEN + "Список миров перезагружен, данные удалены!");
            return true;
        }
        return false;
    }

    private String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}