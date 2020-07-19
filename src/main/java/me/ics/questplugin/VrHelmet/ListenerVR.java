package me.ics.questplugin.VrHelmet;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTp.TeleportatData;
import me.ics.questplugin.HelpClasses.PlayerChecker;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collections;
import java.util.Objects;

public class ListenerVR implements Listener {
    private ItemStack helmet = new ItemStack(Material.IRON_HELMET);
    private Plugin plugin;
    private Objective objective;

    public ListenerVR(Plugin plugin) {
        this.plugin = plugin;
    }

    private ItemStack getVrHelmet(){
        ItemMeta meta = helmet.getItemMeta();
        assert meta != null;
        meta.setUnbreakable(true);
        meta.setDisplayName(ChatColor.DARK_PURPLE + "VR Headset");
        meta.setLore(Collections.singletonList(ChatColor.GOLD + "This headset will teleport you to the game!"));
        helmet.setItemMeta(meta);
        return helmet;
    }

    @EventHandler
    public void join(PlayerJoinEvent event){
        Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getNewScoreboard();
        objective = scoreboard.registerNewObjective("helmet_use", "dummy", "none");
        event.getPlayer().setScoreboard(scoreboard);
    }

    @EventHandler
    public void vrClicked(PlayerMoveEvent event){
        Player player = event.getPlayer();

        if (PlayerChecker.isNotInQuest(player)) return;

        boolean hasVr = Objects.equals(player.getInventory().getHelmet(), getVrHelmet());
        if(hasVr){
            System.out.println(player.getName() + " teleported to parkour!");
            // remove helmet
            player.getInventory().remove(getVrHelmet());
            player.getInventory().setHelmet(null);
            FileJsonEditor<TeleportatData> editor  = new FileJsonEditor<>("/vr.json",
                    new TeleportatData(), plugin);
            //coord of place to tp
            int x = editor.getData().xtp;
            int y = editor.getData().ytp;
            int z = editor.getData().ztp;

            //teleport to pos
            Location loc = new Location(player.getWorld(), x, y, z) ;
            player.teleport(loc);
            player.sendTitle("WELCOME TO THE GAME!", player.getDisplayName(),
                    20, 60, 20);
//            player.sendTitle(color("&cARE YOU READY?"), "", 20, 40, 1);
//            player.sendTitle(color("&eARE YOU?"), "", 1, 20, 1);
//            player.sendTitle(color("&aGO!"), "oh, boy!", 1, 30 ,40);
        }
    }

    @EventHandler
    public void onButton(PlayerInteractEvent event){
        if (PlayerChecker.isNotInQuest(event.getPlayer())) return;

        if (!event.hasBlock()) return;
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) &&
                Objects.requireNonNull(event.getClickedBlock()).
                        getType().equals(Material.BIRCH_BUTTON)) {

            Player player = event.getPlayer();

            Score helmetGivenScore = objective.getScore(event.getPlayer().getName());
            Location loc = event.getClickedBlock().getLocation();
            FileJsonEditor<TeleportatData> editor = new FileJsonEditor<>(
                    "/vr.json", new TeleportatData(), plugin);
            // coords of vr button
            int x = editor.getData().x;
            int y = editor.getData().y;
            int z = editor.getData().z;

            if(loc.getBlockX() == x && loc.getBlockY() == y && loc.getBlockZ() == z){
                //upgrade score
                if(helmetGivenScore.getScore() == 0) {
                    //give helmet
                    player.getInventory().addItem(getVrHelmet());
                    helmetGivenScore.setScore(1);
                }
            }
        }
    }
    private String color(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
