package me.ics.questplugin.TxtWarp;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTxt.ListTxtWarpData;
import me.ics.questplugin.CustomClasses.ClassesTxt.TxtWarpData;
import me.ics.questplugin.HelpClasses.PlayerChecker;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class DelTxtWarp implements CommandExecutor {
    private FileJsonEditor<ListTxtWarpData> editor;

    public DelTxtWarp(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListTxtWarpData(), plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(new PlayerChecker().isOp(sender))) return true;
        if (!(new PlayerChecker().isPlayer(sender))) return true;
        if (! (new PlayerChecker().isAllArgs(sender, 1, args))) return false;
        Player p = (Player) sender;
        String txtWarpName = args[0].toLowerCase();
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