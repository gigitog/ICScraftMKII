package me.ics.questplugin.TpWarp;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTp.ListTeleportsData;
import me.ics.questplugin.CustomClasses.ClassesTp.TeleportatData;
import me.ics.questplugin.HelpClasses.PlayerChecker;
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
        if (PlayerChecker.isNot_Op_AllArgs_Player(sender, 1, args)) return true;
        Player p = (Player) sender;

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

