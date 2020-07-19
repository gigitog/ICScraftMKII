package me.ics.questplugin.HelpClasses;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerChecker {
    public static boolean isNotPlayer(CommandSender sender){
        if(!(sender instanceof Player)){
            System.out.println(ChatColor.RED + "You need to be a player!");
            return true;
        }
        return false;
    }

    public static boolean isNotOp(CommandSender sender){
        if(!sender.isOp()){
            sender.sendMessage(ChatColor.RED + "You don't have permission");
            return true;
        }
        return false;
    }

    public static boolean isNotAllArgs(CommandSender sender, int num, String[] args){
        if(args.length != num){
            sender.sendMessage(ChatColor.RED + "Not all args!");
            return true;
        }
        return false;
    }

    public static boolean isNot_Op_AllArgs_Player(CommandSender sender, int num, String[] args){
        if (isNotOp(sender)) return true;
        return isNotPlayer(sender) || isNotAllArgs(sender, num, args);
    }

    public static boolean isNotInQuest(Player player){
        return !player.getWorld().getName().startsWith("quest");
    }
}
