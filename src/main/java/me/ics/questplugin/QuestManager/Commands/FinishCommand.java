package me.ics.questplugin.QuestManager.Commands;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteQuestData;
import me.ics.questplugin.QuestPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FinishCommand implements CommandExecutor {
    private FileJsonEditor<ListQuestWorldData> editorQuest;

    public FinishCommand(QuestPlugin questPlugin, String fileQuest) {
        editorQuest = new FileJsonEditor<>(fileQuest, new ListQuestWorldData(),questPlugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player)) return false;
        Player player = (Player) sender;
        QuestWorldData questWorldData = editorQuest.getData().getQWDbyPlayer(player.getName());
        if(questWorldData==null){
            player.sendMessage("Вы не в квесте, сеньор...");
            return true;
        }
        questWorldData.checkpoint = 1111;
        RewriteQuestData.rewrite(editorQuest,questWorldData);
        return true;
    }
}
