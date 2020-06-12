package me.ics.questplugin.TxtWarp;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTxt.ListTxtWarpData;
import me.ics.questplugin.CustomClasses.ClassesTxt.TxtWarpData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DelTxtWarp implements CommandExecutor {
    private Plugin plugin;

    public DelTxtWarp(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)){
            System.out.println(color("&cYou need to be a player!"));
            return true;
        }
        Player p = (Player) sender;
        if(!p.isOp()){
            p.sendMessage(color("&cYou don't have permission"));
            return false;
        }
        if(args.length != 1)
            return false;
        String txtWarpName = args[0].toLowerCase();
        //editor
        FileJsonEditor<ListTxtWarpData> editor = new FileJsonEditor<>(
                "/txt_warps_data.json", new ListTxtWarpData(), plugin);
        // list
        ListTxtWarpData listWarps = editor.getData();
        // Search
        for(TxtWarpData txtWarp : listWarps.allData){
            if(txtWarp.name.equals(txtWarpName)){
                listWarps.allData.remove(txtWarp);
                editor.setData(listWarps);
                p.sendMessage(color("&aSuccessfully deleted Txt warp " + txtWarpName));
                return true;
            }
        }
        p.sendMessage(color("&cNo Txt warp with this name!"));
        return true;
    }

    private String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}