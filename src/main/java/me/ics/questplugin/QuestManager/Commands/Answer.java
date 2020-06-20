package me.ics.questplugin.QuestManager.Commands;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.AnswerDataMap;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Cat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;

// Test class
public class Answer implements CommandExecutor {
    private FileJsonEditor<ListQuestWorldData> editorQuest;
    private FileJsonEditor<AnswerDataMap> editorAnswer;

    public Answer(Plugin plugin, String fileQuest, String fileAnswers) {
        editorQuest = new FileJsonEditor<>(fileQuest, new ListQuestWorldData(), plugin);
        editorAnswer = new FileJsonEditor<>(fileAnswers, new AnswerDataMap(), plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        boolean check = false;
        int indexOfQuestWorld = 0;

        if(args.length != 0) {
            ListQuestWorldData listQuestWorlds = editorQuest.getData();
            for(QuestWorldData questWorldData : listQuestWorlds.allQuestWorlds){
                // player answer, his chp, true answer
                String playerAnswer = String.join(" ", args);
                int chp = questWorldData.checkpoint;
                String trueAnswer= editorAnswer.getData().getAnswer(chp);

                //checking
                if(playerAnswer.equals(trueAnswer)){
                    check = true;
                    questWorldData.num_quests_complete.add(chp);
                    player.playSound(player.getLocation(),Sound.ENTITY_PLAYER_LEVELUP, 50,100);
                }

                new RewriteDataInCycle().rewrite(indexOfQuestWorld, questWorldData, editorQuest, check);
            }
        }player.sendMessage("Неправильный ответ!");
        return true;
    }
}
