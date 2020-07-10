package OSclasses;

import org.bukkit.Location;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

import java.util.Map;
import java.util.TreeMap;

public class OSinventories {

    private Location location;

    public OSinventories (Location location){
        this.location = location;
    }

    public Map<Integer, Inventory> createMap(){
        Map<Integer, Inventory> map = new TreeMap<>();
        location.setX(577);location.setY(90);location.setZ(409);
        Inventory computer = ((Chest)location.getBlock().getState()).getBlockInventory();
        map.put(0,computer);
        location.setZ(407);
        Inventory browser = ((Chest)location.getBlock().getState()).getBlockInventory();
        map.put(1,browser);
        location.setZ(405);
        Inventory console = ((Chest)location.getBlock().getState()).getBlockInventory();
        map.put(2,console);
        return map;
    }

}
