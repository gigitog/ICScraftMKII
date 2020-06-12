package me.ics.questplugin.Buttons;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTp.ListTeleportsData;
import me.ics.questplugin.CustomClasses.ClassesTp.TeleportatData;
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
        if(!sender.isOp()){
            sender.sendMessage(color("&cYou don't have permission"));
            return false;
        }
        sender.sendMessage(color("&9&oTp Warps: "));
        //editor
        FileJsonEditor<ListTeleportsData> editor = new FileJsonEditor<>(
                "/buttons_data.json", new ListTeleportsData(), plugin);
        // list
        ListTeleportsData buttons = editor.getData();
        // show warps
        for(TeleportatData button : buttons.allData){
            sender.sendMessage(color("&6" + button.nameTpWarp +
                    ":&9 " + button.x + " " + button.y + " " + button.z));
        }
        return true;
    }

    private String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
