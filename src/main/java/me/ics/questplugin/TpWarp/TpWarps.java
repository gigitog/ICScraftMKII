package me.ics.questplugin.TpWarp;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTp.ListTeleportsData;
import me.ics.questplugin.CustomClasses.ClassesTp.TeleportatData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class TpWarps implements CommandExecutor {
    private Plugin plugin;

    public TpWarps(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp()){
            sender.sendMessage("no perms!");
            return true;
        }

        sender.sendMessage(color("&9&oTp Warps: "));
        //editor
        FileJsonEditor<ListTeleportsData> editor = new FileJsonEditor<>(
                "/tp_warps_data.json", new ListTeleportsData(), plugin);
        // list
        ListTeleportsData tpWarps = editor.getData();
        // show warps
        for(TeleportatData tpWarp : tpWarps.allData){
            sender.sendMessage(color("&6" + tpWarp.nameTpWarp +
                    ":&9 " + tpWarp.x + " " + tpWarp.y + " " +tpWarp.z));
        }
        return true;
    }

    private String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
