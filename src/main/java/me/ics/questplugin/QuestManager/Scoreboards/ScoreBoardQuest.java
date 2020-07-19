package me.ics.questplugin.QuestManager.Scoreboards;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.HelpClasses.PlayerChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

public class ScoreBoardQuest {
    private Map<Integer, String> tasks = new TreeMap<>();

    public static void scoreONPU(Plugin plugin, Player player, ListQuestWorldData listQuestWorldData){
        ScoreboardManager m = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = m.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("onpuboard", "dummy", ChatColor.GREEN + "§lPOLYTECH");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore("").setScore(2);
        objective.getScore(ChatColor.GOLD + "https://ac.opu.ua").setScore(1);

        Team passed = scoreboard.registerNewTeam("Player passed");
        Team freeWorlds = scoreboard.registerNewTeam("FreeWorlds");
        freeWorlds.addEntry(ChatColor.DARK_RED + "" + ChatColor.WHITE);
        passed.addEntry(ChatColor.GREEN + "" + ChatColor.WHITE);

        new BukkitRunnable(){
            @Override
            public void run() {
                int counter = 0;
                int counterWorlds = 0;
                for (QuestWorldData qwd : listQuestWorldData.allQuestWorlds){
                    if (qwd.ticksPlayedFinal != 0) counter++;
                    if (!qwd.isBusy) counterWorlds++;
                }
                objective.getScore("   ").setScore(7);

                freeWorlds.setPrefix(ChatColor.BLUE + "Свободных миров - " + ChatColor.GOLD + "" + counterWorlds);
                objective.getScore(ChatColor.DARK_RED + "" + ChatColor.WHITE).setScore(6);

                objective.getScore("  ").setScore(5);
                objective.getScore(ChatColor.BLUE + "Количество игроков,").setScore(4);

                passed.setPrefix(ChatColor.BLUE + "прошедших квест - " + ChatColor.GOLD + "" + counter);
                objective.getScore(ChatColor.GREEN + "" + ChatColor.WHITE).setScore(3);
            }
        }.runTaskTimer(plugin, 0, 1000);

        player.setScoreboard(scoreboard);
    }

    private void makeTasks() {
        String s = "";
//        tasks.put(201, s + "Таблица истинности");
        tasks.put(201, s + "Граф");
        tasks.put(202, s + "Алгоритм кратчайшего пути");

        tasks.put(301, s + "Статистика");
        tasks.put(302, s + "Вероятность");
        tasks.put(303, s + "Комбинаторика");

        tasks.put(401, s + "Сортировка массива");
        tasks.put(402, s + "Типы данных");
//        tasks.put(403, s + " этапами написания программы");
//        tasks.put(404, s + " основами ООП");

        tasks.put(501, s + "Настройка сервера");
//        tasks.put(502, s + " общими сведениями");

        tasks.put(601, s + " перебором сигналов");
//        tasks.put(602, s + " настройкой робота");
//        tasks.put(603, s + " задачами физики");

        tasks.put(701, s + "Тестирование: Паркур");
        tasks.put(702, s + "Тестирование: PVE");
//        tasks.put(703, s + " передвижением блоков");
    }

    private Function<Integer, String> currentTask = x->{
        if (x < 200) return "Начало квеста";
        if (x > 701 && x < 1050 || x > 1100) return "Конец квеста";
        if (x > 1049 && x < 1100) return ChatColor.DARK_RED  + "Неизвестно";
        return tasks.get(x);
    };

    public void scoreQuest(ListQuestWorldData listQuestWorldData, Plugin plugin, Player player){
        makeTasks();

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("questboard", "dummy", ChatColor.AQUA + "Прохождение Квеста");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Team qVotes = scoreboard.registerNewTeam("Player Votes");
        qVotes.addEntry(ChatColor.RED + "" + ChatColor.WHITE);
        Team pTask = scoreboard.registerNewTeam("Player task");
        pTask.addEntry(ChatColor.YELLOW + "" + ChatColor.WHITE);


        new BukkitRunnable(){
            @Override
            public void run() {
                QuestWorldData qwd = listQuestWorldData.getQWDbyPlayer(player.getName());

                if (qwd == null) return;
                if (PlayerChecker.isNotInQuest(player)) return;
                if (qwd.ticksPlayedFinal == 0) {
                    objective.getScore("  ").setScore(8);
                    objective.getScore(ChatColor.BLUE + "Текущее задание: ").setScore(7);
                    pTask.setPrefix(ChatColor.GOLD + "" + currentTask.apply(qwd.checkpoint));

                    objective.getScore(ChatColor.YELLOW + "" + ChatColor.WHITE).setScore(6);
                }

                objective.getScore(" ").setScore(5);

                Score questVotes = objective.getScore( ChatColor.BLUE + "Ваши оценки: ");
                questVotes.setScore(4);
                if (qwd.checkpoint > 1049 && qwd.checkpoint < 1100){
                    qVotes.setPrefix(ChatColor.DARK_RED + "Неизвестно");
                } else {
                    qVotes.setPrefix("" + Arrays.toString(qwd.votes));
                    objective.getScore("").setScore(2);
                    objective.getScore(ChatColor.GOLD + "https://ac.opu.ua").setScore(1);
                }

                objective.getScore(ChatColor.RED + "" + ChatColor.WHITE).setScore(3);
            }
        }.runTaskTimer(plugin, 0, 20);
        player.setScoreboard(scoreboard);
    }
}
