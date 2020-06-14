package me.ics.questplugin.QuestManager.Commands;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SetCheckpoint implements CommandExecutor {
    private FileJsonEditor<ListQuestWorldData> editor;

    public SetCheckpoint(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp()){
            sender.sendMessage("no perms!");
            return true;
        }
        Player player = (Player) sender;
        ListQuestWorldData listQuest = editor.getData();
        if (args.length != 0) {
            for (QuestWorldData qwi : listQuest.allQuestWorlds) {
                // человек находится в квестовом мире и вводит команду (админ)
                if (qwi.isBusy && qwi.playerName.equalsIgnoreCase(player.getName())) {
                    try {
                        qwi.num_quests_complete.add(Integer.parseInt(args[0]));
                        player.sendMessage(ChatColor.AQUA + "добавлен квест - " + Integer.parseInt(args[0]));
                        new RewriteDataInCycle().rewrite(listQuest.allQuestWorlds.indexOf(qwi), qwi, editor, true);
                    } catch (NumberFormatException e) {
                        player.sendMessage(ChatColor.RED + "Неправильное значение чекпоинта!");
                    }
                    return true;
                } else
                    Bukkit.getLogger().info("Что-то пошло не так при установке чекпоинта :(");
            }
            return true;
        }
        return false;
    }
}
