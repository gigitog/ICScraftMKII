package me.ics.questplugin.OSclasses;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

public class OSinventories {
    private FileJsonEditor<ListQuestWorldData> editorQuest;

    private Inventory computer = Bukkit.createInventory(null,54,"Компьютер");
    private Inventory browser = Bukkit.createInventory(null,54,"Браузер");
    private Inventory console = Bukkit.createInventory(null,54,"Консоль");

    QuestWorldData questWorldData;

    public OSinventories(Plugin plugin, String fileNameQuest, String playerName){
        editorQuest = new FileJsonEditor<>(fileNameQuest, new ListQuestWorldData(), plugin);
        questWorldData = editorQuest.getData().getQWDbyPlayer(playerName);
    }

    public Inventory getInventory(int index){
        Map<Integer, Inventory> map = new TreeMap<>();
        computer.setItem(0,createConsole());
        computer.setItem(8,createBack());
        computer.setItem(18,createBrowser());
        addFrame(computer);
        map.put(0,computer);
        browser.setItem(0,createConsole());
        browser.setItem(8,createBack());
        browser.setItem(18,createBrowser());
        addFrame(browser);
        setBrowserLinks(browser);
        map.put(1,browser);
        console.setItem(0,createConsole());
        console.setItem(8,createBack());
        console.setItem(18,createBrowser());
        addFrame(console);
        setTerminalContain(console);
        map.put(2,console);
        return map.get(index);
    }

    private ItemStack createBack(){
        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta meta = barrier.getItemMeta();
        meta.setDisplayName("закрыть");
        barrier.setItemMeta(meta);
        return barrier;
    }

    private ItemStack createBrowser(){
        ItemStack browser = new ItemStack(Material.SUNFLOWER);
        ItemMeta meta = browser.getItemMeta();
        meta.setDisplayName("браузер");
        browser.setItemMeta(meta);
        return browser;
    }

    private ItemStack createConsole(){
        ItemStack terminal = new ItemStack(Material.DARK_OAK_SIGN);
        ItemMeta meta = terminal.getItemMeta();
        meta.setDisplayName("терминал");
        terminal.setItemMeta(meta);
        return terminal;
    }

    private void addFrame(Inventory inventory){
        ItemStack frame = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta meta = frame.getItemMeta();
        meta.setDisplayName(" ");
        frame.setItemMeta(meta);
        for(int i = 1; i < 54;i += 9){
            inventory.setItem(i,frame);
        }for(int i = 45; i<54;i++){
            inventory.setItem(i,frame);
        }
    }

    private void setBrowserLinks(Inventory browser){
        ItemStack itemStack = new ItemStack(Material.CHISELED_QUARTZ_BLOCK);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(ChatColor.BLUE+"ac.opu.org");
        itemStack.setItemMeta(itemMeta);
        browser.setItem(21,itemStack);
        itemMeta.setDisplayName(ChatColor.BLUE+"ac.opu.ru");
        itemStack.setItemMeta(itemMeta);
        browser.setItem(23,itemStack);
        itemMeta.setDisplayName(ChatColor.BLUE+"ac.opu.ua");
        itemStack.setItemMeta(itemMeta);
        browser.setItem(25,itemStack);
    }

    private void setTerminalContain(Inventory terminal){
        ItemStack itemStack = new ItemStack(Material.RED_TERRACOTTA);
        ItemMeta meta = itemStack.getItemMeta();
        if(questWorldData.osStatus.get(0).equals(false)){
            meta.setDisplayName(ChatColor.RED + "Статус сервера - ВЫКЛ.");
        }else {
            itemStack.setType(Material.GREEN_TERRACOTTA);
            meta.setDisplayName(ChatColor.GREEN + "Статус сервера - ВКЛ.");
        }
        if(questWorldData.osStatus.get(2).equals(false)){
            meta.setLore(Arrays.asList("Статус БД - не подключена"));
        }else meta.setLore(Arrays.asList("Статус БД - подключена"));
        itemStack.setItemMeta(meta);
        terminal.setItem(26,itemStack);
        meta.setLore(Arrays.asList(""));
        itemStack.setItemMeta(meta);
        itemStack.setType(Material.STICK);
        meta = itemStack.getItemMeta();
        meta.setDisplayName("Включить сервер");
        itemStack.setItemMeta(meta);
        terminal.setItem(3,itemStack);
        meta.setDisplayName("Попить водички");
        itemStack.setItemMeta(meta);
        terminal.setItem(12,itemStack);
        meta.setDisplayName("Выключить сервер");
        itemStack.setItemMeta(meta);
        terminal.setItem(21,itemStack);
        meta.setDisplayName("Подключить базу данных");
        itemStack.setItemMeta(meta);
        terminal.setItem(30,itemStack);
        meta.setDisplayName("Включить базу данных");
        itemStack.setItemMeta(meta);
        terminal.setItem(39,itemStack);
    }
}
