package me.ics.questplugin.ArraySorterMine;

import me.ics.questplugin.CustomClasses.ClassesButton.ButtonData;
import me.ics.questplugin.CustomClasses.ClassesButton.ListButtonData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.ListQuestWorldData;
import me.ics.questplugin.CustomClasses.ClassesQuestWorld.QuestWorldData;
import me.ics.questplugin.FileEditor.FileJsonEditor;
import me.ics.questplugin.FileEditor.RewriteDataInCycle;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ListenerArray implements Listener {
    private FileJsonEditor<ListQuestWorldData> editorQuest;
    private FileJsonEditor<ListButtonData> editorButton;
    //contains boolean value if player selected frame
    private Map<String, Boolean> player_selected = new HashMap<>();
    // stores playername and Z coord of the frame
    private Map<String, Integer> player_frame = new HashMap<>();
    // stores playername and if he started the 501
    private Map<String, Boolean> player_started = new HashMap<>();
    // coords of the frame
    private int frameX = 661;
    private int frameY = 89;
    private int frameZ = 380;


    public ListenerArray(Plugin plugin, String fileButton, String fileQuest) {
        editorButton = new FileJsonEditor<>(fileButton, new ListButtonData(), plugin);
        editorQuest = new FileJsonEditor<>(fileQuest, new ListQuestWorldData(), plugin);
    }

    @EventHandler
    public void onArrayButton(PlayerInteractEvent event){
        if(!event.hasBlock()) return;
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) &&
                Objects.requireNonNull(event.getClickedBlock()).
                getType().equals(Material.STONE_BUTTON)){
            Player player = event.getPlayer();
            ListQuestWorldData listWorlds = editorQuest.getData();
            ListButtonData listButtons = editorButton.getData();

            for(QuestWorldData qwd : listWorlds.allQuestWorlds){
                if(qwd.playerName.equals(player.getName())){
                    short counter = 0;
                    if(qwd.checkpoint == 401 && !qwd.num_quests_complete.contains(401)){
                        logicArray(listButtons, event.getClickedBlock().getLocation(), player);
                        Location loc = new Location(player.getWorld(), 662, 91, 382);
                        // роверка на корректность сортировки массива
                        for (int i = 0; i < 5; i++) {
                            Location loc2 = loc.clone();
                            loc2.add(-3 , 2, 0);
                            if(loc.getBlock().getType().equals(loc2.getBlock().getType())){
                                counter++;
                            }
                            loc.add(0, 0, 4);
                        }
                        if(counter == 5){
                            player.sendMessage(ChatColor.GREEN + "Правильно!");
                            qwd.num_quests_complete.add(401);
                            new RewriteDataInCycle().rewrite(listWorlds.allQuestWorlds.indexOf(qwd) , qwd, editorQuest, true);
                            break;
                        }
                    } else return;
                }
            }
        }
    }

    private void logicArray(ListButtonData listButtons, Location locButton, Player player){
        for (ButtonData button : listButtons.allData) {

            // z min = 380 ; z max = 396
            boolean isHere = locButton.getBlockX() == button.x &&
                    locButton.getBlockY() == button.y && locButton.getBlockZ() == button.z;
            if(!isHere) continue;
            String bName = button.nameTpWarp;

            if (bName.equalsIgnoreCase("startArray")){
                player_started.put(player.getName(), true);
                player_frame.put(player.getName(), frameZ);
                player_selected.put(player.getName(), false);

                Location locFrame = new Location(player.getWorld(), frameX, frameY, player_frame.get(player.getName()));
                createColorFrame(locFrame, Material.GRAY_CONCRETE);
            }

            if(!player_started.get(player.getName()))
                return;


            if (bName.equalsIgnoreCase("sel")){
                selFrame(player);
                player.sendMessage("Selected!");
            }

            if (bName.equalsIgnoreCase("lMove")){
                move("left", player);
                player.sendMessage("Left move!");
            }
            if (bName.equalsIgnoreCase("rMove")){
                move("right", player);
                player.sendMessage("Right move!");
            }

        }
    }

    private void move(String direction, Player player) {
        boolean selected = player_selected.get(player.getName());
        int zCoord = player_frame.get(player.getName());
        int zMin = 380;
        int zMax = 396;
        int moveDistance = 0;
        Location locFrame = new Location(player.getWorld(), frameX, frameY, zCoord);

        if (direction.equals("left") && locFrame.getBlockZ() > zMin) {
            moveDistance = -4;
        }
        if (direction.equals("right") && locFrame.getBlockZ() < zMax) {
            moveDistance = 4;
        }


        createColorFrame(locFrame, Material.WHITE_CONCRETE);
        moveFramePos(moveDistance, player.getName());
        locFrame.setZ(player_frame.get(player.getName()));
        if(selected) {
            createColorFrame(locFrame, Material.BLACK_CONCRETE);
            changeBlocks(locFrame, moveDistance);
        }
        else
            createColorFrame(locFrame, Material.GRAY_CONCRETE);
    }

    private void changeBlocks(Location locFrame, int moveDistance){
        Location loc1 = locFrame.clone().add(1, 2, 2);
        Location loc2 = loc1.clone();
        loc2.setZ(loc2.getZ() - moveDistance);
        Material temp = loc1.clone().getBlock().getType();
        loc1.getBlock().setType(loc2.getBlock().getType());
        loc2.getBlock().setType(temp);
    }

    private void selFrame(Player player){
        addPlayerSelected(player.getName());
        Material color = player_selected.get(player.getName()) ? Material.BLACK_CONCRETE : Material.GRAY_CONCRETE;
        Location locFrame = new Location(player.getWorld(), frameX, frameY, player_frame.get(player.getName()));
        createColorFrame(locFrame, color);
    }

    private void createColorFrame(Location locFrame, Material material){
        Location loc = locFrame.clone();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                loc.add(0, i, j);
                if(loc.getBlock().getType().equals(Material.GRAY_CONCRETE) ||
                    loc.getBlock().getType().equals(Material.BLACK_CONCRETE) ||
                    loc.getBlock().getType().equals(Material.WHITE_CONCRETE))
                    loc.getBlock().setType(material);
                loc = locFrame.clone();
            }
        }
    }

    // if player's selection is true, than it turns false.
    // if false, than true
    private void addPlayerSelected(String playerName){
        boolean is = player_selected.get(playerName);
        player_selected.put(playerName, !is);
    }

    private void moveFramePos(int amount, String playerName) {
        player_frame.put(playerName, player_frame.get(playerName) + amount);
    }
}