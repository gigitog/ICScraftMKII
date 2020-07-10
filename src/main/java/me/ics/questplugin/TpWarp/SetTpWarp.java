package me.ics.questplugin.TpWarp;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTp.ListTeleportsData;
import me.ics.questplugin.CustomClasses.ClassesTp.TeleportatData;
import me.ics.questplugin.HelpClasses.PlayerChecker;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SetTpWarp implements CommandExecutor {
    private Plugin plugin;

    public SetTpWarp(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (PlayerChecker.isNot_Op_AllArgs_Player(sender, 4, args)) return false;
        Player p = (Player) sender;

        Location loc  = p.getLocation();
        //write in json file the coords
        //make editor
        FileJsonEditor<ListTeleportsData> editor = new FileJsonEditor<>(
                "/tp_warps_data.json", new ListTeleportsData(), plugin);
        ListTeleportsData tempData = editor.getData();
        // make element of tp warp (current)
        TeleportatData tpWarp = new TeleportatData(args[0].toLowerCase(), loc.getBlockX(),
                loc.getBlockY(), loc.getBlockZ(), Integer.valueOf(args[1]),
                Integer.valueOf(args[2]), Integer.valueOf(args[3]));
        // add to List element
        boolean replace = false;
        int index = 0;
        for(TeleportatData tempTpWarp : tempData.allData){
            if(tempTpWarp.nameTpWarp.equals(tpWarp.nameTpWarp)){
                replace = true;
                index = tempData.allData.indexOf(tempTpWarp);
            }
        }
        if(replace){
            tempData.allData.remove(index);
        }

        tempData.allData.add(tpWarp);
        //write in file the coords + tp coords
        editor.setData(tempData);
        p.sendMessage(ChatColor.GREEN + "Location saved!");
        return true;
    }
}
