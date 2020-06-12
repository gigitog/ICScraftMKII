package me.ics.questplugin.Buttons;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTp.ListTeleportsData;
import me.ics.questplugin.CustomClasses.ClassesTp.TeleportatData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SetButton implements CommandExecutor {
    private Plugin plugin;

    public SetButton(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp()){
            sender.sendMessage(color("&cYou don't have permission"));
            return false;
        }
        if(args.length != 4){
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
            //write in json file the coords + r + str
            //make editor
            FileJsonEditor<ListTeleportsData> editor = new FileJsonEditor<>(
                    "/buttons_data.json", new ListTeleportsData(), plugin);
            ListTeleportsData tempData = editor.getData();
            // make element of tp warp (current)
            TeleportatData button = new TeleportatData(args[0].toLowerCase(), loc.getBlockX(),
                    loc.getBlockY(), loc.getBlockZ(), Integer.valueOf(args[1]),
                    Integer.valueOf(args[2]), Integer.valueOf(args[3]));
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