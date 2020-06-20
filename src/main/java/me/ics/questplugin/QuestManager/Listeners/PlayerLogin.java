package me.ics.questplugin.QuestManager.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PlayerLogin implements Listener {

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        if(!player.isOp())
            return;
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName("§aНачать квест §7(ПКМ)");
        book.setItemMeta(meta);
        ItemStack feather = new ItemStack(Material.FEATHER);
        meta = feather.getItemMeta();
        meta.setDisplayName("§aИнформация о квесте §7(ПКМ)");
        feather.setItemMeta(meta);
        ItemStack bed = new ItemStack(Material.RED_BED);
        meta = bed.getItemMeta();
        meta.setDisplayName("§9Вернуться в лобби §7(ПКМ)");
        bed.setItemMeta(meta);
        bed.setAmount(1);
        feather.setAmount(1);
        book.setAmount(1);

        player.getInventory().setItem(4, book);
        player.getInventory().setItem(7, feather);
        player.getInventory().setItem(8, bed);
    }
}
