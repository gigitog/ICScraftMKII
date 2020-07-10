package me.ics.questplugin.Buttons;

import me.ics.questplugin.CustomClasses.ClassesButton.ButtonData;
import me.ics.questplugin.CustomClasses.ClassesButton.ListButtonData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTp.ListTeleportsData;
import me.ics.questplugin.CustomClasses.ClassesTp.TeleportatData;
import me.ics.questplugin.HelpClasses.PlayerChecker;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class Buttons implements CommandExecutor {
    private Plugin plugin;

    public Buttons(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (PlayerChecker.isNotOp(sender)) return true;

        sender.sendMessage(color("&9&oButtons: "));
        //editor
        FileJsonEditor<ListButtonData> editor = new FileJsonEditor<>(
                "/buttons_data.json", new ListButtonData(), plugin);
        // list
        ListButtonData buttons = editor.getData();
        // show warps
        for(ButtonData button : buttons.allData){
            sender.sendMessage(color("&6" + button.nameTpWarp +
                    ":&9 " + button.x + " " + button.y + " " + button.z + "&r | &bcheckpoint: " + button.checkpoint +
                    " | i: " + button.index + " | v: " + button.value));
        }
        return true;
    }

    private String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
