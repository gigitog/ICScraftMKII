package me.ics.questplugin.OSclasses;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteQuestData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Stack;

public class PlayerComputerInteract implements Listener {
    private FileJsonEditor<ListQuestWorldData> editorQuest;
    private String fileQuest = "/quest_worlds_data";
    private Plugin plugin;
    private ListQuestWorldData listQuestWorldData;

    public PlayerComputerInteract(Plugin plugin, String fileNameQuest, ListQuestWorldData listQuestWorldData){
        editorQuest = new FileJsonEditor<>(fileNameQuest, new ListQuestWorldData(), plugin);
        this.plugin = plugin;
        this.listQuestWorldData = listQuestWorldData;
    }

    @EventHandler
    public void itemsClick(InventoryClickEvent event){
        HumanEntity player = event.getWhoClicked();
        QuestWorldData questWorldData = listQuestWorldData.getQWDbyPlayer(event.getWhoClicked().getName());
        if(questWorldData==null) return;
        if(questWorldData.checkpoint!=501) return;
        ItemStack itemStack = event.getCurrentItem();
        if(itemStack==null) return;
        try{
            Inventory inventory = event.getClickedInventory();
            event.setCancelled(true);
            Stack<Integer> stack = new Stack<>();
            stack.addAll(questWorldData.stack);
            if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("браузер")){
                if(stack.contains(1)){
                    if(stack.size()==3 && stack.peek()==2){
                        stack.add(1,2);stack.pop();
                    }
                }else stack.push(1);
                player.openInventory(new OSinventories(plugin, fileQuest, player.getName(), listQuestWorldData).getInventory(stack.peek()));
            }
            if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("терминал")){
                if(stack.contains(2)){
                    if(stack.size()==3 && stack.peek()==1){
                        stack.add(1,1);stack.pop();
                    }
                }else stack.push(2);
                player.openInventory(new OSinventories(plugin, fileQuest, player.getName(), listQuestWorldData).getInventory(stack.peek()));
            }
            if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("закрыть")){
                if(stack.size()==1){
                    player.closeInventory();
                }else {
                    stack.pop();
                    inventory = new OSinventories(plugin,fileQuest,player.getName(), listQuestWorldData).getInventory(stack.peek());
                    player.openInventory(inventory);
                }
            }
            if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("Включить сервер")){
                inventory = event.getClickedInventory();
                inventory.getItem(26).setType(Material.GREEN_TERRACOTTA);
                ItemMeta meta = inventory.getItem(26).getItemMeta();
                meta.setDisplayName(ChatColor.GREEN + "Статус сервера - ВКЛ.");
                inventory.getItem(26).setItemMeta(meta);
                questWorldData.osStatus.set(0,true);
            }
            if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("Включить базу данных")){
                questWorldData.osStatus.set(1,true);
            }
            if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("Выключить сервер")){
                questWorldData.osStatus.set(0,false);
                questWorldData.osStatus.set(2,false);
                ItemStack item = inventory.getItem(26);
                item.setType(Material.RED_TERRACOTTA);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("Статус сервера - ВЫКЛ.");
                meta.setLore(Arrays.asList("Статус БД - не подключена"));
                item.setItemMeta(meta);
            }
            if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("Подключить базу данных")){
                inventory = event.getClickedInventory();
                ItemStack item = inventory.getItem(26);
                ItemMeta meta = item.getItemMeta();
                if(questWorldData.osStatus.get(1).equals(true) && questWorldData.osStatus.get(0).equals(true)){
                    meta.setLore(Arrays.asList("Статус БД - подключена"));
                    item.setItemMeta(meta);
                    inventory.setItem(26,item);
                    questWorldData.osStatus.set(2,true);
                }
            }
            if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§9ac.opu.ru")){
                for(int i = 11;i < 36;i++){
                    if(i%9==0 || i%8==8 || inventory.getItem(i)!=null) continue;
                    ItemStack item = new ItemStack(Material.RED_TERRACOTTA);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.RED + "HTTP ERROR 403");
                    item.setItemMeta(meta);
                    inventory.setItem(i, item);
                }
            }
            if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§9ac.opu.org")){
                for(int i = 11;i < 36 ;i++){
                    if(i%9==0 || i%8==8 || inventory.getItem(i)!=null) continue;
                    ItemStack item = new ItemStack(Material.RED_TERRACOTTA);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName(ChatColor.RED + "HTTP ERROR 403");
                    item.setItemMeta(meta);
                    inventory.setItem(i, item);
                }
            }
            if(itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§9ac.opu.ua")){
                if(!questWorldData.osStatus.contains(false)){
                    player.closeInventory();
                    ((Player) player).sendTitle(ChatColor.GREEN +"Отлично!", "", 10, 70, 20);
                    questWorldData.num_quests_complete.add(501);
                }else {
                    for(int i = 11;i < 36;i++){
                        if(i%9==0 || i%8==8 || inventory.getItem(i)!=null) continue;
                        ItemStack item = new ItemStack(Material.RED_TERRACOTTA);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName(ChatColor.RED + "HTTP ERROR 404");
                        item.setItemMeta(meta);
                        inventory.setItem(i, item);
                    }
                }
            }
            questWorldData.stack=stack;
            RewriteQuestData.rewrite(listQuestWorldData, questWorldData);
        }catch (NullPointerException e){

        }
    }

}
