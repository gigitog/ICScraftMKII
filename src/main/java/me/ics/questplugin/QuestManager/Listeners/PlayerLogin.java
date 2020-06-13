package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class PlayerLogin implements Listener {
    private FileJsonEditor<ListQuestWorldData> editor;

    public PlayerLogin(Plugin plugin, String fileName) {
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event){
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN+"Начать квест "+ChatColor.GRAY+"(ПКМ)");
        meta.setLore(Arrays.asList("Нажмите ПКМ, держа предмет в руке,", " чтобы начать/продолжить квест."));
        book.setItemMeta(meta);
        book.setAmount(1);
        ItemStack feather = new ItemStack(Material.FEATHER);
        meta = feather.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Информация о квесте "+ChatColor.GRAY+"(ПКМ)");
        meta.setLore(Arrays.asList("Нажмите ПКМ, держа предмет в руке,", " чтобы получить информацию о прохождении квеста."));
        feather.setItemMeta(meta);
        feather.setAmount(1);
        ItemStack bed = new ItemStack(Material.RED_BED);
        meta = bed.getItemMeta();
        meta.setDisplayName(ChatColor.BLUE + "Вернуться в лобби "+ChatColor.GRAY+"(ПКМ)");
        meta.setLore(Arrays.asList("Нажмите ПКМ, держа предмет в руке,"," чтобы вернуться в лобби."));
        bed.setAmount(1);
        bed.setItemMeta(meta);
        event.getPlayer().getInventory().setItem(4,book);
        event.getPlayer().getInventory().setItem(7,feather);
        event.getPlayer().getInventory().setItem(8,bed);
    }
}
