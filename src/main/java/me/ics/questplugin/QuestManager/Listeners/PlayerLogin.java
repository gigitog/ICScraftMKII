package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.HelpClasses.QuestInstruments;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PlayerLogin implements Listener {
    List<String> strings = Arrays.asList("§aНачать квест §7(ПКМ)","§aИнформация о квесте §7(ПКМ)","§9Вернуться в лобби §7(ПКМ)");

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(!player.isOp()) {
            for(ItemStack item : player.getInventory().getContents()){
                if (strings.contains(Objects.requireNonNull(item.getItemMeta()).getDisplayName())) {
                    item.setAmount(0);
                }
            }
            return;
        }

        player.getInventory().setItem(4, new QuestInstruments().makeQuestBook());
        if(player.isOp()) player.getInventory().setItem(7, new QuestInstruments().makeStatsFeather());
        player.getInventory().setItem(8, new QuestInstruments().makeLobbyBed());
    }
}
