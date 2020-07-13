package me.ics.questplugin.QuestManager.Listeners;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.List;

public class ArmorManipulate implements Listener {
    private FileJsonEditor<ListQuestWorldData> editor;

    public ArmorManipulate(Plugin plugin, String fileName){
        editor = new FileJsonEditor<>(fileName, new ListQuestWorldData(), plugin);
    }

    @EventHandler
    public void armorManipulate(PlayerArmorStandManipulateEvent event){
        QuestWorldData questWorldData = editor.getData().getQWDbyPlayer(event.getPlayer().getName());
        if(questWorldData==null || questWorldData.ticksPlayedFinal!=0 || questWorldData.checkpoint!=303 ) return;
        event.setCancelled(true);
        EquipmentSlot slot = event.getSlot();
        List<Material> helmets = Arrays.asList(Material.DIAMOND_HELMET,Material.LEATHER_HELMET,Material.CHAINMAIL_HELMET);
        List<Material> chestplates = Arrays.asList(Material.DIAMOND_CHESTPLATE,Material.GOLDEN_CHESTPLATE,Material.IRON_CHESTPLATE);
        List<Material> leggins = Arrays.asList(Material.LEATHER_LEGGINGS,Material.IRON_LEGGINGS,Material.GOLDEN_LEGGINGS);
        List<Material> feets = Arrays.asList(Material.GOLDEN_BOOTS,Material.LEATHER_BOOTS,Material.IRON_BOOTS);
        int counter;
        Material material = event.getArmorStandItem().getType();
        if(slot.equals(EquipmentSlot.HEAD)){
            counter = helmets.indexOf(material);
            counter(counter);
            event.getRightClicked().setHelmet(new ItemStack(helmets.get(counter)));
        }
        if(slot.equals(EquipmentSlot.CHEST)){
            counter = chestplates.indexOf(material);
            counter(counter);
            event.getRightClicked().setChestplate(new ItemStack(chestplates.get(counter)));
        }
        if(slot.equals(EquipmentSlot.LEGS)){
            counter = leggins.indexOf(material);
            counter(counter);
            event.getRightClicked().setLeggings(new ItemStack(leggins.get(counter)));
        }
        if(slot.equals(EquipmentSlot.FEET)){
            counter = feets.indexOf(material);
            counter(counter);
            event.getRightClicked().setBoots(new ItemStack(feets.get(counter)));
        }
    }

    private int counter(int counter){
        if(counter==2) counter = 0;
        else counter++;
        return counter;
    }

}
