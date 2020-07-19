package me.ics.questplugin.TxtWarp;

import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.CustomClasses.ClassesTxt.ListTxtWarpData;
import me.ics.questplugin.CustomClasses.ClassesTxt.TxtWarpData;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ListenerTxt implements Listener {
    private Map<String, Boolean> places = new HashMap<>();
    private Plugin plugin;
    private ListTxtWarpData tempData;
    public ListenerTxt(Plugin plugin, String fileTxt) {
        this.plugin = plugin;
        FileJsonEditor<ListTxtWarpData> editor = new FileJsonEditor<>(fileTxt, new ListTxtWarpData(), plugin);
        tempData = editor.getData();
    }

    private boolean placesContain(String name_place) {
        if (places.get(name_place) == null)
            return false;
        return places.get(name_place);
    }

    // add playerName_placeName to map and make it True
    private void add(String name_place) {
        places.put(name_place, true);
    }

    // playerName_placeName set false
    private void remove(String name_place) {
        places.put(name_place, false);
    }

    @EventHandler
    public void onTxtWarp(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location loc = event.getTo();

        for (TxtWarpData txtWarp : tempData.allData) {
            assert loc != null;
            boolean x = Math.abs(loc.getBlockX() - txtWarp.x) <= txtWarp.radius;
            boolean y = Math.abs(loc.getBlockY() - txtWarp.y) <= 1;
            boolean z = Math.abs(loc.getBlockZ() - txtWarp.z) <= txtWarp.radius;
            // get name of the place of the iteration
            // make "name"_"place" name - nick, place - name of the area
            String name_place = player.getName().concat("_".concat(txtWarp.name));
            if (x && y && z) {
                if (!placesContain(name_place)) {
                    //if it's player's first step in the region, make name_place in map true
                    add(name_place);

                    if (txtWarp.name.equalsIgnoreCase("finalyura1")){
                        player.sendTitle(ChatColor.GOLD + "ВЫБОР КОНЦОВКИ", color("&cНеизвестная&r | &9Обычная    &r"), 40, 100, 80);
                    }
                    if (txtWarp.name.equalsIgnoreCase("finalyura33")){
                        player.getInventory().clear();
                    }

                    //send text that is in file
                    List<String> s = Arrays.asList(txtWarp.text.split(" "));
                    String to_send;
                    if (s.contains("//")) {
                        to_send = String.join(" ", s.subList(0, s.indexOf("//")));
                        player.sendMessage(color(to_send));
                        List<Integer> index_of_slash = new ArrayList<>();
                        for (int i = 0; i < s.size(); i++) {
                            if (s.get(i).equals("//"))
                                index_of_slash.add(i);
                        }

                        for (int i = 0; i < index_of_slash.size(); i++) {
                            int finalI = i;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    String to_send;
                                    int last;
                                    if (finalI == index_of_slash.size() - 1)
                                        last = s.size();
                                    else
                                        last = index_of_slash.get(finalI + 1);
                                    to_send = String.join(" ", s.subList(index_of_slash.get(finalI) + 1, last));
                                    player.sendMessage(color(to_send));
                                }
                            }.runTaskLaterAsynchronously(plugin, 120 * (i + 1));

                        }

                    } else {
                        player.sendMessage(color(txtWarp.text));
                    }
                    break;
                }
                return;
            }
        }
    }

    private String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}