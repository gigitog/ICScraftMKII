package me.ics.questplugin.QuestManager.Commands;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
import me.ics.questplugin.FileEditor.RewriteQuestData;
import me.ics.questplugin.HelpClasses.PlayerChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Stack;

public class SetCheckpoint implements CommandExecutor {
    private FileJsonEditor<ListQuestWorldData> editor;

    public SetCheckpoint(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (PlayerChecker.isNot_Op_AllArgs_Player(sender, 1, args)) return false;
        Player player = (Player) sender;
        ListQuestWorldData listQuest = editor.getData();
        QuestWorldData questWorldData = listQuest.getQWDbyPlayer(player.getName());

        try {
            questWorldData.checkpoint = Integer.parseInt(args[0]);
            player.sendMessage(ChatColor.AQUA + "добавлен квест - " + Integer.parseInt(args[0]));
            RewriteQuestData.rewrite(editor, questWorldData);
        } catch (NumberFormatException e) {
            player.sendMessage(ChatColor.RED + "Неправильное значение чекпоинта!");
        }
        return true;
    }
}
