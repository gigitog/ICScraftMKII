package me.ics.questplugin.VrHelmet;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTp.TeleportatData;
import me.ics.questplugin.HelpClasses.PlayerChecker;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class SetVrHelmetButton implements CommandExecutor {
    private Plugin plugin;

    public SetVrHelmetButton(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(new PlayerChecker().isOp(sender))) return true;
        if (!(new PlayerChecker().isPlayer(sender))) return true;
        Player p = (Player) sender;

        Location loc = p.getLocation();
        loc.add(0, 1, 1);
        loc.getBlock().setType(Material.STONE);
        loc.add(0, 0, -1);
        loc.getBlock().setType(Material.BIRCH_BUTTON);

        //write in config file the coords of button
        FileJsonEditor<TeleportatData> reader = new FileJsonEditor<>("/vr.json",
                new TeleportatData(), plugin);
        TeleportatData temp = reader.getData();

        TeleportatData v = new TeleportatData("", loc.getBlockX(),
                loc.getBlockY(), loc.getBlockZ(), temp.xtp, temp.ytp, temp.ztp);

        reader.setData(v);

        p.sendMessage(ChatColor.GREEN + "Location saved!");
        return true;
    }
}