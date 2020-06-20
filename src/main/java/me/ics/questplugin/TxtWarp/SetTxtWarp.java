package me.ics.questplugin.TxtWarp;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTxt.ListTxtWarpData;
import me.ics.questplugin.CustomClasses.ClassesTxt.TxtWarpData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SetTxtWarp implements CommandExecutor {
    private FileJsonEditor<ListTxtWarpData> editor;
    private  Plugin plugin;

    public SetTxtWarp(Plugin plugin, String fileName) {
        this.plugin = plugin;
        editor = new FileJsonEditor<>(fileName, new ListTxtWarpData(), plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length < 3){
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
            String txtName = args[0].toLowerCase();
            // make shown text as args[2:]
            int index =  txtName.length() + args[1].length() + args[2].length() + 3;
            String text = String.join(" ", args).substring(index);

            //write in json file the coords + r + str
            // get data previous
            ListTxtWarpData tempData = editor.getData();
            // make element of txt warp (current)
            TxtWarpData textWarp = new TxtWarpData(txtName, loc.getBlockX(),
                    loc.getBlockY(), loc.getBlockZ(), Integer.valueOf(args[1]),
                    Integer.valueOf(args[2]), text);
            // add to List element
            tempData.allData.add(textWarp);

            // write in file
            editor.setData(tempData);

            p.sendMessage(ChatColor.GREEN + "Location saved!");
            return true;
        } else {
            System.out.println("You need to be a player to use that");
            return true;
        }
    }
}
