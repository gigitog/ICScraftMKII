package me.ics.questplugin.HelpClasses;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerChecker {
    public boolean isPlayer(CommandSender sender){
        if(!(sender instanceof Player)){
            System.out.println(ChatColor.RED + "You need to be a player!");
            return true;
        }
        return true;
    }

    public boolean isOp(CommandSender sender){
        if(!sender.isOp()){
            sender.sendMessage(ChatColor.RED + "You don't have permission");
            return false;
        }
        return true;
    }

    public boolean isAllArgs(CommandSender sender, int num, String[] args){
        if(args.length != num){
            sender.sendMessage(ChatColor.RED + "Not all args!");
            return false;
        }
        return true;
    }
}
