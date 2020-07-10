package OSclasses;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class PlayerComputerInteract implements Listener {
    private FileJsonEditor<ListQuestWorldData> editorQuest;

    public PlayerComputerInteract(Plugin plugin, String fileNameQuest){
        editorQuest = new FileJsonEditor<>(fileNameQuest, new ListQuestWorldData(), plugin);
    }

    @EventHandler
    public void itemsClick(InventoryClickEvent event){
        QuestWorldData questWorldData = editorQuest.getData().getQWDbyPlayer(event.getWhoClicked().getName());
        if(questWorldData==null) return;
        if(questWorldData.checkpoint!=501) return;
        ItemStack itemStack = event.getCurrentItem();
        if(itemStack==null) return;

    }

}
