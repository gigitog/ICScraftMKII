package me.ics.questplugin.Buttons;

import me.ics.questplugin.CustomClasses.ClassesButton.ButtonData;
import me.ics.questplugin.CustomClasses.ClassesButton.ListButtonData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SetButton implements CommandExecutor {
    private FileJsonEditor<ListButtonData> editor;

    public SetButton(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListButtonData(), plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp()){
            sender.sendMessage(color("&cYou don't have permission"));
            return false;
        }
        if(args.length != 4 && args.length != 7){
            sender.sendMessage("not all args!");
            return false;
        }
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(!p.isOp()) {
                p.sendMessage("no perms!");
                return true;
            }
            Location loc  = p.getLocation();
            //building a structure with button
            loc.add(0, 1, 1);
            loc.getBlock().setType(Material.WHITE_WOOL);
            loc.add(0, 0, -1);
            Material b = Material.STONE_BUTTON;
            loc.getBlock().setType(Material.STONE_BUTTON);

            String buttonName = args[0].toLowerCase();
            //write in json file the coords
            ListButtonData tempData = editor.getData();
            // make element of tp warp (current)
            ButtonData button = new ButtonData();
            if(args.length == 7){
                 button = new ButtonData(args[0].toLowerCase(), loc.getBlockX(),
                        loc.getBlockY(), loc.getBlockZ(), Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]), Integer.parseInt(args[3]),
                        Integer.parseInt(args[4]), Integer.parseInt(args[5]),
                        Integer.parseInt(args[6]));
            } else {
                button = new ButtonData(args[0].toLowerCase(), loc.getBlockX(),
                        loc.getBlockY(), loc.getBlockZ(), -1, -1, -1,
                        Integer.parseInt(args[1]), Integer.parseInt(args[2]),
                        Integer.parseInt(args[3]));
            }
            // add to List element
            tempData.allData.add(button);
            //write in file the coords + tp coords
            editor.setData(tempData);
            p.sendMessage(ChatColor.GREEN + "Location saved!");
            return true;
        } else {
            System.out.println("You need to be a player to use that");
            return true;
        }
    }
    private String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}