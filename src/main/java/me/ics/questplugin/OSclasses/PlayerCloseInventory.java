package me.ics.questplugin.OSclasses;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteQuestData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.Plugin;


public class PlayerCloseInventory implements Listener {
    private FileJsonEditor<ListQuestWorldData> editorQuest;
    private ListQuestWorldData listQuestWorldData;

    public PlayerCloseInventory(Plugin plugin, String fileNameQuest, ListQuestWorldData listQuestWorldData){
        editorQuest = new FileJsonEditor<>(fileNameQuest, new ListQuestWorldData(), plugin);
        this.listQuestWorldData = listQuestWorldData;
    }


    @EventHandler
    public void ComputerClose(InventoryCloseEvent event){
        String player = event.getPlayer().getName();
        QuestWorldData questWorldData = listQuestWorldData.getQWDbyPlayer(player);
        if(questWorldData==null) return;
        if(questWorldData.checkpoint!=501) return;
        questWorldData.stack.removeAllElements();
        questWorldData.stack.push(0);
        RewriteQuestData.rewrite(listQuestWorldData,questWorldData);
    }
}
