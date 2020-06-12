package me.ics.questplugin.TpWarp;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTp.ListTeleportsData;
import me.ics.questplugin.CustomClasses.ClassesTp.TeleportatData;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DelTpWarp implements CommandExecutor {
    private Plugin plugin;

    public DelTpWarp(Plugin plugin) {
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
        // get name of tp warp
        String tpWarpName = args[0].toLowerCase();
        // editor
        FileJsonEditor<ListTeleportsData> editor = new FileJsonEditor<>(
                "/tp_warps_data.json", new ListTeleportsData(), plugin);
        // list
        ListTeleportsData listWarps = editor.getData();
        // search
        for(TeleportatData tpWarp : listWarps.allData){
            if(tpWarp.nameTpWarp.equals(tpWarpName)){
                listWarps.allData.remove(tpWarp);
                editor.setData(listWarps);
                p.sendMessage(color("&aSuccessfully deleted Tp warp &9" + tpWarpName));
                return true;
            }
        }
        p.sendMessage(color("&cNo Tp warp with this name!"));
        return true;
    }

    private String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}

