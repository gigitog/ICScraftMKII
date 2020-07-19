package me.ics.questplugin.HelpClasses;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class QuestInstruments {

    public QuestInstruments(){

    }

    public static ItemStack makeQuestBook(){
        ItemStack book = new ItemStack(Material.BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName("§aНачать квест §7(ПКМ)");
        book.setItemMeta(meta);
        return book;
    }

    public  static ItemStack makeLobbyBed(){
        ItemStack bed = new ItemStack(Material.RED_BED);
        ItemMeta meta = bed.getItemMeta();
        meta.setDisplayName("§9Вернуться в лобби §7(ПКМ)");
        bed.setItemMeta(meta);
        return bed;
    }

    public static ItemStack makeStatsFeather(){
        ItemStack feather = new ItemStack(Material.FEATHER);
        ItemMeta meta = feather.getItemMeta();
        meta.setDisplayName("§aИнформация о квесте §7(ПКМ)");
        feather.setItemMeta(meta);
        return feather;
    }

    public static ItemStack makeEndRedstone(){
        ItemStack redstone = new ItemStack(Material.REDSTONE);
        ItemMeta meta = redstone.getItemMeta();
        meta.setDisplayName("§aЗакончить квест §7(ПКМ)");
        meta.setLore(Arrays.asList("Аварийное прекращение квеста с ", "получением результата."));
        redstone.setItemMeta(meta);
        return redstone;
    }
}
