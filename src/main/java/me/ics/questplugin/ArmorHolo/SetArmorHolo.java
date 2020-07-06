package me.ics.questplugin.ArmorHolo;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetArmorHolo implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.isOp()) return false;
        if(args.length < 2) return  false;
        int counterLength = 0;
        int counterStrings = 0;
        Player player = Bukkit.getPlayer(args[0]);

        for (String arg : args) {
            counterLength += arg.length();
            if (counterLength > 30 || arg.equals("//")) {
                counterLength = 0;
                counterStrings++;
            }
        }
        if(counterStrings > 10) {
            assert player != null;
            player.sendMessage("Слишком большой текст, " + counterStrings + " строк!");
            return true;
        }
        assert player != null;
        String concat = "";
        int index = 0;
        for (String arg : args) {
            concat = concat.concat(arg + " ");
            if (counterLength > 30 || arg.equals("//")) {
                counterLength = 0;
                index++;
                player.performCommand("summon armor_stand " + player.getLocation().getX() + " " +
                        (player.getLocation().getY() + ((counterStrings / 2) - (0.27) * index)) + " " + player.getLocation().getZ() +
                        " {Invisible:1b,NoGravity:1b,CustomName:\"{\\\"text\\\":\\\"" + concat + "\\\"}\",CustomNameVisible:1b}");
                concat = "";
            }
            counterLength += arg.length();
        }
        index++;
        player.performCommand("summon armor_stand " + player.getLocation().getX() + " " +
                (player.getLocation().getY() + ((counterStrings/2)-(0.27)*index))  + " " + player.getLocation().getZ() +
                " {Invisible:1b,NoGravity:1b,CustomName:\"{\\\"text\\\":\\\"" + concat + "\\\"}\",CustomNameVisible:1b}");
        return true;
    }
}
