package me.ics.questplugin.QuestManager.Scoreboards;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
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

    public static void scoreONPU(Plugin plugin, Player player, FileJsonEditor<ListQuestWorldData> editor){
        ScoreboardManager m = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = m.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("onpuboard", "dummy", ChatColor.GREEN + "§lPOLYTECH");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        objective.getScore("").setScore(2);
        objective.getScore(ChatColor.GOLD + "https://ac.opu.ua").setScore(1);

        Team passed = scoreboard.registerNewTeam("Player passed");
        passed.addEntry(ChatColor.GREEN + "" + ChatColor.WHITE);

        new BukkitRunnable(){
            @Override
            public void run() {
                int counter = 0;
                for (QuestWorldData qwd : editor.getData().allQuestWorlds){
                    if (qwd.ticksPlayedFinal != 0) counter++;
                }

                objective.getScore("  ").setScore(5);
                objective.getScore(ChatColor.AQUA + "Количество игроков,").setScore(4);
                passed.setPrefix(ChatColor.AQUA + "прошедших квест - " + ChatColor.GOLD + "" + counter);

                objective.getScore(ChatColor.GREEN + "" + ChatColor.WHITE).setScore(3);
            }
        }.runTaskTimer(plugin, 0, 1000);

        player.setScoreboard(scoreboard);
    }

    private void makeTasks() {
        String s = "";
//        tasks.put(201, s + "Таблица истинности");
        tasks.put(202, s + "Граф");
        tasks.put(203, s + "Алгоритм кратчайшего пути");

        tasks.put(301, s + "Статистика");
        tasks.put(302, s + "Вероятность");
        tasks.put(303, s + "Комбинаторика");

        tasks.put(401, s + "Сортировка массива");
        tasks.put(402, s + "Типы данных");
//        tasks.put(403, s + " этапами написания программы");
//        tasks.put(404, s + " основами ООП");

        tasks.put(501, s + "Настройка сервера");
//        tasks.put(502, s + " общими сведениями");

//        tasks.put(601, s + "о сбором компьтера");
//        tasks.put(602, s + " настройкой робота");
//        tasks.put(603, s + " задачами физики");

        tasks.put(701, s + "Тестирование: Паркур");
        tasks.put(702, s + "Тестирование: PVE");
//        tasks.put(703, s + " передвижением блоков");
    }

    private Function<Integer, String> currentTask = x->{
        if (x < 202) return "Начало квеста";
        if (x > 710 && x < 1050) return "Конец квеста";
        if (x > 1049) return ChatColor.DARK_RED  + "Неизвестно";
        return tasks.get(x);
    };

    public void scoreQuest(FileJsonEditor<ListQuestWorldData> editorQuest, Plugin plugin, Player player){
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
                QuestWorldData qwd = editorQuest.getData().getQWDbyPlayer(player.getName());

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
                if (qwd.checkpoint > 1049){
                    qVotes.setPrefix(ChatColor.DARK_RED + "Неизвестно");
                } else {
                    qVotes.setPrefix("" + Arrays.toString(qwd.votes));
                    objective.getScore("").setScore(2);
                    objective.getScore(ChatColor.GOLD + "https://ac.opu.ua").setScore(1);
                }

                objective.getScore(ChatColor.RED + "" + ChatColor.WHITE).setScore(3);
            }
        }.runTaskTimer(plugin, 0, 10);
        player.setScoreboard(scoreboard);
    }
}
