package me.ics.questplugin.QuestManager.Commands;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.CustomClasses.Statistic.ListAllStatsData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.QuestManager.Scoreboards.ScoreBoardQuest;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public class QuestOperator implements CommandExecutor {
    private FileJsonEditor<ListQuestWorldData> editor;
    private FileJsonEditor<ListAllStatsData> editorStats;
    private Plugin plugin;

    public QuestOperator(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
        editorStats = new FileJsonEditor<>("/stats.txt", new ListAllStatsData(), plugin);
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)){
            sendInfoToConsole();
            if (createWorld(args)) return true;
            return true;
        }

        Player player = (Player) sender;
        QuestWorldData questWorldData = editor.getData().getQWDbyPlayer(player.getName());

        //    /quest
        if (rawQuestCommand(args, player, questWorldData))
            return true;
        if (!player.isOp()) return true;
        //    /quest status
        if (status(args, player)) return true;
        //    /quest create xxx
        if (createWorld(args)) return true;
        //    /quest worldlist
        if (worldList(args, player)) return true;
        //    /quest playerlist
        if (playerList(args, player)) return true;
        //    /quest remove xxx
        if (removeWorld(args, player)) return true;
        return false;
    }

    private void sendInfoToConsole() {
        ListQuestWorldData listQuestWorldData = editor.getData();
        int counter = 0;
        List<String> playerList = new ArrayList<>();
        for (QuestWorldData questWorldData : listQuestWorldData.allQuestWorlds) {
            if (!questWorldData.isBusy) counter++;
            else playerList.add(questWorldData.playerName);
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GOLD + "Количество свободных миров - " + ChatColor.GREEN + counter);
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "Игроки: ");
        Bukkit.getConsoleSender().sendMessage(playerList.toString());
    }

    //    /quest
    private boolean rawQuestCommand(String[] args, Player player, QuestWorldData questWorldData) {
        if (args.length == 0) {
            ListQuestWorldData listQuestWorldData = editor.getData();
            if(questWorldData!=null) {
                if (questWorldData.ticksPlayedFinal == 0) {
                    if (player.getWorld().getName().equalsIgnoreCase(questWorldData.questWorldName)) {
                        player.sendMessage("Вы уже в квесте!");
                        return true;
                    }
                    Location saved = new Location(Bukkit.getWorld(questWorldData.questWorldName), questWorldData.spawn[0], questWorldData.spawn[1], questWorldData.spawn[2]);
                    new ScoreBoardQuest().scoreQuest(editor, plugin, player);
                    player.getInventory().clear();

                    player.sendMessage(ChatColor.GREEN + "Телепортация на сохраненную локацию.");
                    player.teleport(saved);
                    return true;
                }
                if (questWorldData.playerName.equalsIgnoreCase(player.getName())) {
                    player.sendMessage("Вы уже прошли квест");
                    return true;
                }
                return true;
            }
            for (QuestWorldData questData : listQuestWorldData.allQuestWorlds) {
                if (!questData.isBusy) {
                    int index = listQuestWorldData.allQuestWorlds.indexOf(questData);
                    questWorldData = questData;
                    World quest = Bukkit.getWorld(questData.questWorldName);
                    if(quest==null) continue;
                    questWorldData.isBusy = true;
                    questWorldData.playerName = player.getName();
                    questWorldData.ticksLivedWhenStart = player.getTicksLived();
                    questWorldData.ticksSavedBeforeLeaving = 0;
                    listQuestWorldData.allQuestWorlds.set(index,questWorldData);
                    Location loc_spawn = new Location(quest, questData.spawn[0], questData.spawn[1], questData.spawn[2]);
                    quest.setTime(23180);

                    player.teleport(loc_spawn);
                    new ScoreBoardQuest().scoreQuest(editor, plugin, player);

                    loc_spawn.setX(0);
                    loc_spawn.setY(1);
                    loc_spawn.setZ(0);
                    player.setGameMode(GameMode.ADVENTURE);
                    if(player.getInventory().getItem(4)!=null){
                        Objects.requireNonNull(player.getInventory().getItem(4)).setAmount(0);
                    }
                    editor.setData(listQuestWorldData);
                    return true;
                }
            }
            player.sendMessage(ChatColor.RED + "Свободные миры для квеста закончились! Подожди, пока освободится место!");
            return true;
        }
        return false;
    }

    //    /quest status
    private boolean status(String[] args, Player player){
        if (args.length == 2 && args[0].equalsIgnoreCase("status")) {
            String name = args[1];
            QuestWorldData questWorldData = editor.getData().getQWDbyPlayer(name);
            if(questWorldData==null){
                player.sendMessage(ChatColor.RED + "Игрок не находится/находился в квесте!");
                return true;
            }
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
        return false;
    }

    //    /quest create xxx
    private boolean createWorld(String[] args) {
        if (args.length == 2 && args[0].equals("create")) {
            try {
                Integer num = Integer.valueOf(args[1]);
                if (num < 1 || num > 5) {
//                    player.sendMessage(ChatColor.RED + "Не стоит этого делать");

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
//                            player.performCommand("mv clone worldquest " + name);
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "mv clone worldquest " + name);
                            Thread.sleep(250);
                            QuestWorldData questWorldData = new QuestWorldData(Objects.requireNonNull(Bukkit.getWorld(name)));
                            ListQuestWorldData listQuestWorldData = editor.getData();
                            listQuestWorldData.allQuestWorlds.add(questWorldData);
                            // запись в файл
                            editor.setData(listQuestWorldData);
                            break;
                        }
                    }
                }
                return true;
            } catch (NumberFormatException | InterruptedException e) {
//                player.sendMessage(ChatColor.RED + "Неправильное значение количества миров!");
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
    private boolean worldList(String[] args, Player player){
        // вывод всех миров
        ListQuestWorldData listQuestWorlds = editor.getData();
        if (args.length == 1 && args[0].equalsIgnoreCase("worldlist")) {
            if (listQuestWorlds.allQuestWorlds.size() == 0) {
                player.sendMessage(ChatColor.DARK_PURPLE + "Список миров для квеста пуст!");
                return true;
            }

            int counter = 0;
            for(QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds){
                if(!questWorldData.isBusy) counter++;
            }
            player.sendMessage(ChatColor.GREEN + "Пройдено - " + (listQuestWorlds.allQuestWorlds.size()-counter) +". Свободно - "+ counter +".");

            for (QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds)
                player.sendMessage(color(passed.apply(questWorldData)));
            return true;
        }
        return false;
    }

    //    /quest playerlist
    private boolean playerList(String[] args, Player player) {
        if(args.length==1 && args[0].equalsIgnoreCase("playerlist")){
            ListQuestWorldData listQuestWorldData = editor.getData();
            if (listQuestWorldData.allQuestWorlds.size() == 0) {
                player.sendMessage(ChatColor.RED + "Никто не проходил квест!");
                return true;
            }
            player.sendMessage(ChatColor.DARK_GREEN + "Список игроков: ");
            for(QuestWorldData questWorldData : listQuestWorldData.allQuestWorlds){
                if(questWorldData.isBusy){
                    if(questWorldData.ticksPlayedFinal==0){
                        player.sendMessage("Игрок "+ChatColor.GREEN +  questWorldData.playerName+ChatColor.RESET + " играет в мире " +questWorldData.questWorldName);
                    } else player.sendMessage("Игрок "+ChatColor.GREEN +  questWorldData.playerName+ChatColor.RESET + " прошел квест в мире " +questWorldData.questWorldName);
                }
            }
            return true;
        }
        return false;
    }

    //    /quest remove xxx
    private boolean removeWorld(String[] args,Player player){
        // удаление мира
        if (args.length == 2 && args[0].equalsIgnoreCase("remove")) {
            ListQuestWorldData listQuestWorlds = editor.getData();
            String worldOrPlayerName = args[1];
            for (QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds) {
                if (questWorldData.questWorldName.equals(worldOrPlayerName) && !questWorldData.isBusy) {
                    //удаление и перезапись
                    listQuestWorlds.allQuestWorlds.remove(questWorldData);
                    editor.setData(listQuestWorlds);
                    player.sendMessage(ChatColor.GREEN + "Пустая запись в мире " +
                            worldOrPlayerName + " успешно удалена!");
                    return true;
                }
                if(questWorldData.playerName.equals(worldOrPlayerName)){
                    listQuestWorlds.allQuestWorlds.remove(questWorldData);
                    editor.setData(listQuestWorlds);
                    player.sendMessage(ChatColor.GREEN + "Запись о прохождения квеста игроком " +
                            worldOrPlayerName + " успешно удалена!");
                    return true;
                }
            }
            player.sendMessage(ChatColor.RED + "Такого мира или игрока нет!");
            return true;
        }
        return false;
    }

    private String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}