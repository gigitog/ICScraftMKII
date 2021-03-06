package me.ics.questplugin.QuestManager.Commands;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.AnswerDataMap;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
import me.ics.questplugin.FileEditor.RewriteQuestData;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class Answer implements CommandExecutor {
    private FileJsonEditor<ListQuestWorldData> editorQuest;
    private FileJsonEditor<AnswerDataMap> editorAnswer;
    private ListQuestWorldData listQuestWorldData;

    public Answer(Plugin plugin, String fileQuest, String fileAnswers, ListQuestWorldData listQuestWorldData) {
        editorQuest = new FileJsonEditor<>(fileQuest, new ListQuestWorldData(), plugin);
        editorAnswer = new FileJsonEditor<>(fileAnswers, new AnswerDataMap(), plugin);
        this.listQuestWorldData = listQuestWorldData;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;

        if (args.length != 0) {
            QuestWorldData questWorldData = listQuestWorldData.getQWDbyPlayer(player.getName());
            String playerAnswer = String.join(" ", args);
            int chp = questWorldData.checkpoint;
            String trueAnswer = editorAnswer.getData().getAnswer(chp);
            //checking
            if (playerAnswer.equalsIgnoreCase(trueAnswer)) {

                // avoid dublicates
                if (questWorldData.num_quests_complete.contains(chp)) {
                    player.sendMessage(ChatColor.GREEN + "Это ответ на прошлое задание! Отвечать можно лишь на текущее.");
                    return true;
                }
                questWorldData.num_quests_complete.add(chp);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 50, 100);
                player.sendMessage(ChatColor.GREEN + "Правильный ответ!");
            } else player.sendMessage(ChatColor.RED + "Неправильный ответ!");
            RewriteQuestData.rewrite(listQuestWorldData, questWorldData);
        }
        return true;
    }
}
