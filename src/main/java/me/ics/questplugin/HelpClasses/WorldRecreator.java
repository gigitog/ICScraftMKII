package me.ics.questplugin.HelpClasses;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;

public class WorldRecreator {

    private World world;

    public WorldRecreator(World world){
        this.world = world;
    }

    public void worldRecreator(){
        graphClear();
        chest202Clear();
        arrayClear();
        swordInFrame();
        chestFinalClear();
    }

    private void graphClear() {
        for (int x = 651; x < 668; x++) {
            for (int y = 72; y < 78; y++) {
                for (int z = 455; z < 467; z++) {
                    Location wool = new Location(world, x, y, z);
                    if (wool.getBlock().getType().equals(Material.LIME_WOOL)) {
                        wool.getBlock().setType(Material.RED_WOOL);
                    }
                }
            }
        }
    }

    private void chest202Clear() {
        Location location = new Location(world,656,75,448);
        Chest chest = (Chest)location.getBlock().getState();
        for(ItemStack itemStack : chest.getBlockInventory().getContents()){
            if (itemStack != null){
                if(itemStack.getType().equals(Material.LIME_WOOL)) itemStack.setAmount(0);
            }
        }
        location.setZ(449);
        chest = (Chest)location.getBlock().getState();
        for(ItemStack itemStack : chest.getBlockInventory().getContents()){
            if (itemStack != null){
                if(itemStack.getType().equals(Material.LIME_WOOL)) itemStack.setAmount(0);
            }
        }
        chest.getBlockInventory().setItem(26,new ItemStack(Material.LIME_WOOL));
    }

    private void arrayClear() {
        int x = 661;
        for (int y = 89; y < 94; y++) {
            for (int z = 380; z < 400; z++) {
                Location block = new Location(world, x, y, z);
                if (block.getBlock().getType().equals(Material.BLACK_CONCRETE) || block.getBlock().getType().equals(Material.GRAY_CONCRETE)){
                    block.getBlock().setType(Material.WHITE_CONCRETE);
                }
            }
        }
    }

    private void swordInFrame() {
        Location block = new Location(world, 654, 93, 367);
        Collection<Entity> collection = world.getNearbyEntities(block,1,1,1);
        for(Entity entity : collection){
            if(entity instanceof ItemFrame){
                ItemFrame frame = (ItemFrame)entity;
                frame.setItem(new ItemStack(Material.STONE_SWORD));
            }
        }
    }

    private void chestFinalClear() {
        Location location = new Location(world,532,66,666);
        Chest chest = (Chest)location.getBlock().getState();
        chest.getBlockInventory().clear();
    }
}