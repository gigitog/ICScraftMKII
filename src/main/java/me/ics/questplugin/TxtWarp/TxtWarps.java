package me.ics.questplugin.TxtWarp;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTxt.ListTxtWarpData;
import me.ics.questplugin.CustomClasses.ClassesTxt.TxtWarpData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

public class TxtWarps implements CommandExecutor {
    private Plugin plugin;

    public TxtWarps(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp()){
            sender.sendMessage("no perms!");
            return true;
        }
        sender.sendMessage(color("&9&oTxt Warps: "));
        //editor
        FileJsonEditor<ListTxtWarpData> editor = new FileJsonEditor<>(
                "/txt_warps_data.json", new ListTxtWarpData(), plugin);
        // list
        ListTxtWarpData txtWarps = editor.getData();
        // show warps
        for(TxtWarpData txtWarp : txtWarps.allData){
            sender.sendMessage(color("&6" + txtWarp.name +
                    ":&9 " + txtWarp.x + " " + txtWarp.y + " " + txtWarp.z));
        }
        return true;
    }

    private String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
