package me.ics.questplugin.Buttons;

import me.ics.questplugin.CustomClasses.ClassesButton.ButtonData;
import me.ics.questplugin.CustomClasses.ClassesButton.ListButtonData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
import me.ics.questplugin.FileEditor.RewriteQuestData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ListenerButton implements Listener {
    private FileJsonEditor<ListButtonData> editorButton;
    private FileJsonEditor<ListQuestWorldData> editorQuest;
    private Plugin plugin;
    private Map<String, Boolean> player_clicked = new HashMap<>();
    private ListQuestWorldData listQuestWorldData;

    public ListenerButton(Plugin plugin, String fileButton, String fileQuest, ListQuestWorldData listQuestWorldData) {
        this.plugin = plugin;
        editorButton = new FileJsonEditor<>(fileButton, new ListButtonData(), plugin);
        editorQuest = new FileJsonEditor<>(fileQuest, new ListQuestWorldData(), plugin);
        this.listQuestWorldData = listQuestWorldData;
    }
    @EventHandler
    public void onButton(PlayerInteractEvent event){
        if(!event.hasBlock()) return;
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) &&
                Objects.requireNonNull(event.getClickedBlock()).
                        getType().equals(Material.STONE_BUTTON)){

            Player player = event.getPlayer();
            Location loc = event.getClickedBlock().getLocation();
            Location locTp = new Location(event.getPlayer().getWorld(),0,0,0, 0, 0);

            //list
            ListButtonData buttons = editorButton.getData();

            // search
            for(ButtonData button : buttons.allData){
                // condition that player clicked on button in file
                boolean isHere = loc.getBlockX() == button.x &&
                        loc.getBlockY() == button.y && loc.getBlockZ() == button.z;
                if(isHere){
                    boolean canTp = true;
                    // добавляем в индекс массива оценок голос игрока по поводу данной секции
                    QuestWorldData qwd = listQuestWorldData.getQWDbyPlayer(player.getName());
                    if (qwd == null) return;

                    if(button.checkpoint > qwd.checkpoint){
                        player.sendMessage(ChatColor.RED + "Туда еще рано!");
                        return;
                    }
                    if(button.index != -1 && button.value != -1){
                        qwd.votes[button.index] = button.value;
                        RewriteQuestData.rewrite(listQuestWorldData, qwd);
                    }

                    canTp = isCanTp(player, loc, locTp, button, true, event);

                    // set Location, Yaw and Pitch then tp
                    locTp.add(button.xtp + 0.5, button.ytp, button.ztp + 0.5);
                    locTp.setYaw(loc.getYaw());
                    locTp.setPitch(loc.getPitch());

                    if(canTp) player.teleport(locTp);
                }
            }
        }
    }

    private boolean isCanTp(Player player, Location loc, Location locTp, ButtonData button, boolean canTp, PlayerInteractEvent event) {
        String bName = button.nameTpWarp;
        boolean is = bName.equalsIgnoreCase("startArray") ||
                bName.equalsIgnoreCase("lMove") ||
                bName.equalsIgnoreCase("rMove") ||
                bName.equalsIgnoreCase("sel");
        if (is) canTp = false;

        if (bName.length() == 4 && bName.contains("to")){
            if (player_clicked.containsKey(player.getUniqueId().toString())){
                if (player_clicked.get(player.getUniqueId().toString())){
                    event.setCancelled(true);
                    return false;
                }
            }

            List<String> str = Arrays.asList(bName.split(""));
            player.sendMessage("Лифт подымается. " + str.get(3) + " этаж");
            canTp = false;
            tpElevator(player, loc, locTp, button, 45);
            // игрок нажал кнопку, ставим true
            player_clicked.put(player.getUniqueId().toString(), true);
        }

        if (bName.equalsIgnoreCase("9tohell")){
            if (player_clicked.containsKey(player.getUniqueId().toString())){
                if (player_clicked.get(player.getUniqueId().toString())){
                    event.setCancelled(true);
                    return false;
                }
            }

            player.sendMessage("Лифт спускается");
            canTp = false;
            new BukkitRunnable() {
                @Override
                public void run() {
                    player_clicked.put(player.getUniqueId().toString(), false);
                }
            }.runTaskLater(plugin, 520);

            for (int i = 0; i < 12; i++) {
                int finalI = i;
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if(finalI <= 9)
                            player.sendMessage("§a" + (9-finalI) + " этаж");
                        else
                            player.sendMessage("§a§o????§r§a этаж");
                    }
                }.runTaskLater(plugin, (i*40 - (int) Math.pow(1.52, i)));
            }
            tpElevator(player, loc, locTp, button, 515);
            player_clicked.put(player.getUniqueId().toString(), true);
        }

        if(button.nameTpWarp.startsWith("none")) canTp = false;

        return canTp;
    }

    private void tpElevator(Player player, Location loc, Location locTp, ButtonData button, int delay) {
        new BukkitRunnable(){
            @Override
            public void run() {
                locTp.setX(button.xtp + 0.5);
                locTp.setY(button.ytp);
                locTp.setZ(button.ztp + 0.5);
                locTp.setYaw(loc.getYaw());
                locTp.setPitch(loc.getPitch());
                player.teleport(locTp);
                player_clicked.put(player.getUniqueId().toString(), false);
            }
        }.runTaskLater(plugin, delay);
    }
}
