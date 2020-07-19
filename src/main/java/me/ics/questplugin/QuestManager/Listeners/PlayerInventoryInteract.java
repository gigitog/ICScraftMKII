package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteQuestData;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PlayerInventoryInteract implements Listener {
    private List<String> noInteractItems = Arrays.asList("§aЗакончить квест §7(ПКМ)","§aНачать квест §7(ПКМ)","§aИнформация о квесте §7(ПКМ)","§9Вернуться в лобби §7(ПКМ)");
    private FileJsonEditor<ListQuestWorldData> editorQuest;
    public PlayerInventoryInteract(Plugin plugin, String fileNameQuest){
        editorQuest = new FileJsonEditor<>(fileNameQuest, new ListQuestWorldData(), plugin);
    }

    @EventHandler
    public void onPlayerInventoryInteract(InventoryClickEvent event){
        Player player = (Player) event.getWhoClicked();
        //проверка на мир

        QuestWorldData questWorldData = editorQuest.getData().getQWDbyPlayer(player.getName());
        if(questWorldData!=null) {
            if(!player.getWorld().getName().startsWith("quest")) return;
            if(event.getClickedInventory()==null || !event.getClickedInventory().getType().equals(InventoryType.CHEST)) return;
            if (questWorldData.checkpoint == 202 && !questWorldData.num_quests_complete.contains(202)){

                if (questWorldData.counter >= 19) {
                    player.sendTitle("§cПровал!", "§cМожно было за 18 ходов", 10, 40, 10);
                    player.closeInventory();
                    return;
                }
                Inventory chest = event.getClickedInventory();
                try {
                    if (event.getCurrentItem().getType().equals(Material.BARRIER)) {
                        event.setCancelled(true);
                        return;
                    }
                    if (event.getCurrentItem().getType().equals(Material.LIME_WOOL)) {
                        event.setCancelled(true);
                        return;
                    }
                    if (event.getCurrentItem().getType().equals(Material.DIAMOND_BLOCK)) {
                        event.setCancelled(true);
                        return;
                    }
                } catch (NullPointerException e) {
                }

                List<Integer> list = makePossibleIndexArray(event.getSlot());
                boolean lime = false;
                boolean finish = false;

                for (int indexInChest : list) {
                    if (chest.getItem(indexInChest) == null) continue;
                    if (chest.getItem(indexInChest).getType().equals(Material.DIAMOND_BLOCK)) {
                        finish = true;
                    }
                    if (chest.getItem(indexInChest).getType().equals(Material.LIME_WOOL)) {
                        questWorldData.counter++;
                        lime = true;
                        event.setCancelled(true);
                        chest.setItem(event.getSlot(), new ItemStack(Material.LIME_WOOL));
                        RewriteQuestData.rewrite(editorQuest,questWorldData);
                    }
                }

                if (lime && finish) {
                    player.closeInventory();
                    if (questWorldData.counter == 18) {
                        player.sendTitle("§aМолодец", "", 10, 40, 10);
                        questWorldData.num_quests_complete.add(202);
                    }
                    RewriteQuestData.rewrite(editorQuest,questWorldData);
                    return;
                } else if (lime) {
                    return;
                }
                player.sendMessage("а сюда нельзя");
            } else if (event.getClickedInventory().getType().equals(InventoryType.CHEST) && questWorldData.checkpoint / 100 == 2 && (questWorldData.num_quests_complete.contains(202) || questWorldData.counter >= 0)) {
                player.closeInventory();
                return;
            }
        }
        if(event.getCurrentItem() == null)
            return;
        if(event.getCurrentItem().getType().equals(Material.AIR)) {
            return;
        }
        if(noInteractItems.contains(Objects.requireNonNull(event.getCurrentItem().getItemMeta()).getDisplayName())){
            event.setCancelled(true);
        }
    }

    private List<Integer> makePossibleIndexArray(int indexOfSlot) {
        List<Integer> List;
        if (indexOfSlot == 0) {
            List = Arrays.asList(1, 9);
        } else if (indexOfSlot == 8) {
            List = Arrays.asList(7, 17);
        } else if (indexOfSlot == 45) {
            List = Arrays.asList(36, 46);
        } else if (indexOfSlot == 53) {
            List = Arrays.asList(44, 52);
        } else if (indexOfSlot % 9 == 0) {
            List = Arrays.asList(indexOfSlot - 9, indexOfSlot + 9, indexOfSlot + 1);
        } else if (indexOfSlot % 9 == 8) {
            List = Arrays.asList(indexOfSlot - 9, indexOfSlot + 9, indexOfSlot - 1);
        } else if (indexOfSlot < 8) {
            List = Arrays.asList(indexOfSlot - 1, indexOfSlot + 1, indexOfSlot + 9);
        } else if (indexOfSlot > 45) {
            List = Arrays.asList(indexOfSlot - 1, indexOfSlot + 1, indexOfSlot - 9);
        } else List = Arrays.asList(indexOfSlot - 1, indexOfSlot + 1, indexOfSlot + 9, indexOfSlot - 9);
        return List;
    }
}
