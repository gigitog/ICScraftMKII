package OSclasses;

import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.Arrays;
import java.util.Objects;
import java.util.Stack;

public class PlayerClickOnComp implements Listener {
    private FileJsonEditor<ListQuestWorldData> editorQuest;

    public PlayerClickOnComp(Plugin plugin, String fileNameQuest){
        editorQuest = new FileJsonEditor<>(fileNameQuest, new ListQuestWorldData(), plugin);
    }

    @EventHandler
    public void PlayerClick(PlayerInteractEvent event){
        if(!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && Objects.requireNonNull(event.getClickedBlock()).getType().equals(Material.PLAYER_HEAD))) return;
        Player player = event.getPlayer();
        QuestWorldData questWorldData = editorQuest.getData().getQWDbyPlayer(player.getName());
        if(questWorldData==null) return;
        if(questWorldData.checkpoint!=501) return;
        Stack<Integer> inventoryStack = new Stack<>();
        inventoryStack.addAll(questWorldData.stack);
        player.openInventory(new OSinventories(player.getLocation()).createMap().get(inventoryStack.peek()));
    }
}
