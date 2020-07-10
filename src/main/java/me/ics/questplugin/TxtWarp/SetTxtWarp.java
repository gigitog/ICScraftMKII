package me.ics.questplugin.TxtWarp;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTxt.ListTxtWarpData;
import me.ics.questplugin.CustomClasses.ClassesTxt.TxtWarpData;
import me.ics.questplugin.HelpClasses.PlayerChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SetTxtWarp implements CommandExecutor {
    private FileJsonEditor<ListTxtWarpData> editor;

    public SetTxtWarp(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListTxtWarpData(), plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0 || args.length == 2) {
            sender.sendMessage("not all args!");
            return false;
        }
        if (PlayerChecker.isNotOp(sender)) return true;

        Location loc = new Location(sender.getServer().getWorld("world"), 0, 0, 0);
        if (sender instanceof Player) {
            loc = ((Player) sender).getLocation();
        } else if (sender instanceof CommandBlock) {
            Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "CommandBlock!");
            loc = ((CommandBlock) sender).getLocation();

        }

        String txtName = args[0].toLowerCase();
        // make shown text as args[2:]
        ListTxtWarpData tempData = editor.getData();

        // make element of txt warp (current)
        if (args.length > 3) {
            int index = txtName.length() + args[1].length() + args[2].length() + 3;
            String text = String.join(" ", args).substring(index);
            for (TxtWarpData warp : tempData.allData) {
                if (warp.name.equals(txtName)) {
                    sender.sendMessage(ChatColor.RED + "Такой варп существует!");
                    return true;
                }
            }
            //write in json file the coords + r + str
            // get data previous
            TxtWarpData textWarp = new TxtWarpData(txtName, loc.getBlockX(),
                    loc.getBlockY(), loc.getBlockZ(), Integer.valueOf(args[1]),
                    Integer.valueOf(args[2]), text);
            // add to List element
            tempData.allData.add(textWarp);

            // write in file
            editor.setData(tempData);
        } else {
            TxtWarpData textWarp = null;
            boolean replace = false;
            int i = 0;
            for (TxtWarpData warp : tempData.allData) {
                if (warp.name.equals(args[0])) {
                    warp.x = loc.getBlockX();
                    warp.y = loc.getBlockY();
                    warp.z = loc.getBlockZ();
                    textWarp = warp;
                    replace = true;
                    i = tempData.allData.indexOf(warp);
                }
            }
            if (replace) {
                tempData.allData.remove(i);
                tempData.allData.add(textWarp);
                // write in file
                editor.setData(tempData);
            }
        }
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Location saved!");
        return true;
    }
}