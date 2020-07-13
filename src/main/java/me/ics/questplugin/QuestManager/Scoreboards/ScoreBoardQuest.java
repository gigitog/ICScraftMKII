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

public class ScoreBoardQuest {

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

    public static void scoreQuest(FileJsonEditor<ListQuestWorldData> editorQuest, Plugin plugin, Player player){

        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective("questboard", "dummy", ChatColor.AQUA + "Прохождение Квеста");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        Team qVotes = scoreboard.registerNewTeam("Player Votes");
        qVotes.addEntry(ChatColor.RED + "" + ChatColor.WHITE);
        Team pTask = scoreboard.registerNewTeam("Player task");
        pTask.addEntry(ChatColor.YELLOW + "" + ChatColor.WHITE);

        objective.getScore("").setScore(2);
        objective.getScore(ChatColor.GOLD + "https://ac.opu.ua").setScore(1);

        new BukkitRunnable(){
            @Override

            public void run() {
                QuestWorldData qwd = editorQuest.getData().getQWDbyPlayer(player.getName());

                if (qwd == null) return;
                if (PlayerChecker.isNotInQuest(player)) return;
                if (qwd.ticksPlayedFinal == 0) {
                    objective.getScore("  ").setScore(8);
                    objective.getScore(ChatColor.BLUE + "Текущее задание: ").setScore(7);
                    pTask.setPrefix(ChatColor.GOLD + "" + qwd.checkpoint);

                    objective.getScore(ChatColor.YELLOW + "" + ChatColor.WHITE).setScore(6);
                }

                objective.getScore(" ").setScore(5);

                Score questVotes = objective.getScore( ChatColor.BLUE + "Ваши оценки: ");
                questVotes.setScore(4);

                qVotes.setPrefix("" + Arrays.toString(qwd.votes));
                objective.getScore(ChatColor.RED + "" + ChatColor.WHITE).setScore(3);

            }
        }.runTaskTimer(plugin, 0, 10);
        player.setScoreboard(scoreboard);
    }
}
