package me.ics.questplugin.QuestManager.Commands;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestStats;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QuestOperator implements CommandExecutor {
    private FileJsonEditor<ListQuestWorldData> editor;

    public QuestOperator(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        ListQuestWorldData listQuestWorlds = editor.getData();
        Player player = (Player) sender;
        int indexOfQuestWorld = 0;
        QuestWorldData tempQuestData = new QuestWorldData(player.getWorld());
        if (args.length == 0) {
            if (listQuestWorlds.allQuestWorlds.size() != 0) {
                for (QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds) {
                    if (questWorldData.isBusy && questWorldData.playerName.equalsIgnoreCase(player.getName()) && questWorldData.ticksPlayedFinal == 0) {
                        Location saved = new Location(Bukkit.getWorld(questWorldData.questWorldName), questWorldData.spawn[0], questWorldData.spawn[1], questWorldData.spawn[2]);
                        player.sendMessage(ChatColor.GREEN + "Телепортация на сохраненную локацию.");
                        player.teleport(saved);
                        return true;
                    }
                    if (questWorldData.ticksPlayedFinal != 0 && questWorldData.playerName.equalsIgnoreCase(player.getName())) {
                        int i = (int) (Math.random() * 10);
                        switch (i) {
                            case 1:
                                break;
                        }
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
                        quest.setTime(12500);
                        player.teleport(loc_spawn);
                        loc_spawn.setX(0);
                        loc_spawn.setY(1);
                        loc_spawn.setZ(0);
                        loc_spawn.getBlock().setType(Material.REDSTONE_BLOCK);
                        player.setGameMode(GameMode.ADVENTURE);
                        player.getInventory().setItem(4, null);
                        break;
                    }
                }
                listQuestWorlds.allQuestWorlds.remove(indexOfQuestWorld);
                listQuestWorlds.allQuestWorlds.add(indexOfQuestWorld, tempQuestData);
                editor.setData(listQuestWorlds);
                return true;
            }
            player.sendMessage(ChatColor.RED + "Свободные миры для квеста закончились!");
            return true;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("status")) {
            String name = args[1];
            for (QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds) {
                // информация о мире и игроке
                if (questWorldData.isBusy && questWorldData.playerName.equalsIgnoreCase(name)) {
                    QuestStats qs = new QuestStats(editor, name);
                    player.getInventory().setItem(0, qs.makeBook());

                    player.sendMessage(ChatColor.GREEN + "Квестовый мир - " + questWorldData.questWorldName);
                    player.sendMessage(ChatColor.GREEN + "Игрок - " + questWorldData.playerName);
                    player.sendMessage(ChatColor.GREEN + "Текущий квест - " + questWorldData.checkpoint);
                    player.sendMessage(ChatColor.GREEN + "Пройденные квесты - " + questWorldData.num_quests_complete.toString());
                    player.sendMessage(ChatColor.BLUE + "Оценки: " + Arrays.toString(questWorldData.votes));

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
        // вывод всех миров
        if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
            if (listQuestWorlds.allQuestWorlds.size() == 0) {
                player.sendMessage(ChatColor.DARK_PURPLE + "Список миров для квеста пуст!");
                return true;
            }
            for (QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds) {
                if (questWorldData.isBusy) {
                    player.sendMessage(ChatColor.RED + questWorldData.questWorldName +
                            ChatColor.RED + " - занят игроком " + questWorldData.playerName + ".");
                } else
                    player.sendMessage(ChatColor.GREEN + questWorldData.questWorldName +
                            ChatColor.GREEN + " - свободен.");
            }

            return true;
        }
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
            for (QuestWorldData qwd : tempDataToDelete) {
                listQuestWorlds.allQuestWorlds.remove(qwd);
            }
            editor.setData(listQuestWorlds);

            player.sendMessage(ChatColor.GREEN + "Список миров перезагружен, данные удалены!");
            return true;
        }
        // удаление мира
        if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            String playerName = player.getName();
            for (QuestWorldData qwi : listQuestWorlds.allQuestWorlds) {
                if (qwi.isBusy && qwi.playerName.equalsIgnoreCase(playerName)) {
                    //удаление и перезапись
                    listQuestWorlds.allQuestWorlds.remove(qwi);
                    editor.setData(listQuestWorlds);
                    player.sendMessage(ChatColor.GREEN + "Запись о прохождении квеста игроком " +
                            playerName + ChatColor.GREEN + " успешно удалена!");
                    return true;
                }
            }
            player.sendMessage(ChatColor.RED + "Игрок не проходил квест!");
            return true;
        }
        return false;
    }
}