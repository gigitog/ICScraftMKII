package me.ics.questplugin.FrameItemChoose;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
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
        for (QuestWorldData qwd : editorQuest.getData().allQuestWorlds) {
            if (qwd.playerName.equals(event.getPlayer().getName()) && qwd.checkpoint == 402){
                event.setCancelled(true);
                if (Objects.requireNonNull(event.getInventory().getItem(0)).getType().equals(Material.IRON_AXE)){
                    event.getPlayer().sendMessage("Iron AXE");
                } else if (Objects.requireNonNull(event.getInventory().getItem(0)).getType().equals(Material.POTATO)){
                    event.getPlayer().sendMessage("Potato");
                } else if (Objects.requireNonNull(event.getInventory().getItem(0)).getType().equals(Material.STONE)){
                    event.getPlayer().sendMessage("STONKS");
                }
            }
        }
    }

}
