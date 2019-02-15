package tech.zuosi.deadbydaylight.data;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iwar on 2016/7/9.
 */
public class StructureMap {
    private Map<Location,Location> doorGenerateLocation = new HashMap<>();
    private Map<Location,FurnaceProperty> furnacePropertyMap = new HashMap<>();
    private List<Location> trapLocationList = new ArrayList<>();

    public StructureMap() {}

    public void storeDoorGenerateLocation(Location buttonLocation,Location doorLocation) {
        doorGenerateLocation.put(buttonLocation,doorLocation);
    }

    public Location queryDoorGenerateLocation(Location buttonLocation) {
        if (!doorGenerateLocation.containsKey(buttonLocation)) {
            //±¨´í
            return null;
        }
        return doorGenerateLocation.get(buttonLocation);
    }

    public void storeFurnaceProperty(Location furnaceLocation,FurnaceProperty furnaceProperty) {
        furnacePropertyMap.put(furnaceLocation,furnaceProperty);
    }

    public FurnaceProperty queryFurnaceProperty(Location furnaceLocation) {
        if (!furnacePropertyMap.containsKey(furnaceLocation)) {
            furnacePropertyMap.put(furnaceLocation,new FurnaceProperty().BURN_TIME(0).COOK_TIME(0));
        }
        return furnacePropertyMap.get(furnaceLocation);
    }

    public void storeTrapLocation(Location trapLocation) {
        trapLocationList.add(trapLocation);
    }

    public boolean removeTrapLocation(Location trapLocation) {
        return trapLocationList.remove(trapLocation);
    }

    public Location queryTrapLocation(Location fenceLocation) {
        for (Location trapLocation : trapLocationList) {
            if (fenceLocation.distance(trapLocation) <= 2.0) {
                return trapLocation;
            }
        }
        return null;
    }
}
