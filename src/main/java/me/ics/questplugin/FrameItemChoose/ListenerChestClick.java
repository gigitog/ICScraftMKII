package me.ics.questplugin.FrameItemChoose;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
import me.ics.questplugin.FileEditor.RewriteQuestData;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ListenerChestClick implements Listener {
    private FileJsonEditor<ListQuestWorldData> editorQuest;
    private List<ItemStack> items = Arrays.asList(
            new ItemStack(Material.BOOKSHELF),
            new ItemStack(Material.SWEET_BERRIES),
            new ItemStack(Material.BOW),
            new ItemStack(Material.APPLE),

            new ItemStack(Material.GOLDEN_PICKAXE),
            new ItemStack(Material.END_STONE_BRICK_STAIRS),
            new ItemStack(Material.FLINT_AND_STEEL),
            new ItemStack(Material.ROTTEN_FLESH),
            new ItemStack(Material.FURNACE),

            new ItemStack(Material.SPIDER_EYE),
            new ItemStack(Material.SHEARS),
            new ItemStack(Material.DRIED_KELP_BLOCK),
            new ItemStack(Material.HONEY_BOTTLE),
            new ItemStack(Material.DAMAGED_ANVIL)
    );
    private List<Integer> itemsIndex = Arrays.asList(1, 2, 3, 1, 3, 1, 2, 1, 3, 2, 3, 1, 2, 3, 2);

    public ListenerChestClick(FileJsonEditor<ListQuestWorldData> editorQuest) {
        this.editorQuest = editorQuest;
    }

    @EventHandler
    public void onClick(InventoryOpenEvent event){
        QuestWorldData questWorldData = editorQuest.getData().getQWDbyPlayer(event.getPlayer().getName());
        if(questWorldData!=null){
            if(questWorldData.checkpoint == 402 && !questWorldData.num_quests_complete.contains(402)){
                if(event.getInventory().getType().equals(InventoryType.CHEST)) event.setCancelled(true);
                else return;
                ItemFrame frame = null;
                QuestWorldData tempData = questWorldData;
                for(Entity entity : event.getPlayer().getNearbyEntities(10,3,10)){
                    if ((entity instanceof ItemFrame) && entity.getLocation().getY()>=93) frame = (ItemFrame)entity;
                }
                if (Objects.requireNonNull(event.getInventory().getItem(0)).getType().equals(Material.IRON_AXE)){
                    if(itemsIndex.get(questWorldData.counter)==1){
                        frame.setItem(items.get(questWorldData.counter));
                        tempData.counter = questWorldData.counter+1;
                    }
                }else if (Objects.requireNonNull(event.getInventory().getItem(0)).getType().equals(Material.STONE)){
                    if(itemsIndex.get(questWorldData.counter)==2){
                        if(questWorldData.counter==14){
                            event.getPlayer().sendMessage(ChatColor.GREEN + "Отлично!");
                            tempData.counter = 0;
                            tempData.num_quests_complete.add(402);
                            frame.setItem(new ItemStack(Material.AIR));
                        }else {
                            frame.setItem(items.get(questWorldData.counter));
                            tempData.counter = questWorldData.counter+1;
                        }
                    }
                }else if (Objects.requireNonNull(event.getInventory().getItem(0)).getType().equals(Material.POTATO)){
                    if(itemsIndex.get(questWorldData.counter)==3){
                        frame.setItem(items.get(questWorldData.counter));
                        tempData.counter = questWorldData.counter+1;
                    }
                }
                RewriteQuestData.rewrite(editorQuest,questWorldData);
            }
        }
    }
}
