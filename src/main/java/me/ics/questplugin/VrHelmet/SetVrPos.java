package me.ics.questplugin.VrHelmet;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTp.TeleportatData;
import me.ics.questplugin.HelpClasses.PlayerChecker;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SetVrPos implements CommandExecutor {
    private Plugin plugin;

    public SetVrPos(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (PlayerChecker.isNotOp(sender)) return true;
        if (PlayerChecker.isNotPlayer(sender)) return true;

        Player p = (Player) sender;

        FileJsonEditor<TeleportatData> editor = new FileJsonEditor<>(
                "/vr.json", new TeleportatData(), plugin);
        TeleportatData tempData = editor.getData();

        if (args.length == 0) {
            Location loc = p.getLocation();
            TeleportatData tpWarp = new TeleportatData("",
                    tempData.x, tempData.y, tempData.z,
                    loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
            editor.setData(tpWarp);
            p.sendMessage(ChatColor.GREEN + "Location saved!");
            return true;
        } else if (args.length == 3) {
            TeleportatData tpWarp = new TeleportatData("", tempData.x,
                    tempData.y, tempData.z,
                    Integer.parseInt(args[0]),
                    Integer.parseInt(args[1]),
                    Integer.parseInt(args[2]));
            editor.setData(tpWarp);
            p.sendMessage(ChatColor.GREEN + "Location saved!");
            return true;
        }
        return false;
    }
}

