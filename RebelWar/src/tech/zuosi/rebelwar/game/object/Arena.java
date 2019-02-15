package tech.zuosi.rebelwar.game.object;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import tech.zuosi.rebelwar.game.manager.QueueManager;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by iwar on 2016/9/29.
 */

@SerializableAs("Arena")
public class Arena implements ConfigurationSerializable {
    private String name;
    private Location mainLocationDown;
    private Location mainLocationUp;
    private Location subLocationDown;
    private Location subLocationUp;
    private Set<Location> chestSet = new HashSet<>();
    private Location mainTp,subTp;

    public Arena() {
        this.name = "Default";
    }

    //LATER 把Loction转化为Vector

    /**
     * 调用后面的方法来给Arena对象完善信息
     * @param name
     */
    public Arena(String name) {
        this.name = name;
    }

    private void tryAddToQueue() {
        if (check()) QueueManager.getINSTANCE().addToQueue(this.name,this);
    }

    public void setMainLocation(Location up,Location down) {
        this.mainLocationUp = up;
        this.mainLocationDown = down;
        tryAddToQueue();
    }

    public void setSubLocation(Location up,Location down) {
        this.subLocationUp = up;
        this.subLocationDown = down;
        tryAddToQueue();
    }

    public boolean addChestLocation(Location loc) {
        boolean result = this.chestSet.add(loc);
        tryAddToQueue();
        return result;
    }

    public boolean removeChestLocation(Location loc) {
        if (QueueManager.getINSTANCE().getStates().containsKey(name)) {
            QueueManager.getINSTANCE().removeFromQueue(name);
        }
        return this.chestSet.remove(loc);
    }

    public Location getMainTp() {
        return mainTp;
    }

    public void setMainTp(Location mainTp) {
        this.mainTp = mainTp;
        tryAddToQueue();
    }

    public Location getSubTp() {
        return subTp;
    }

    public void setSubTp(Location subTp) {
        this.subTp = subTp;
        tryAddToQueue();
    }

    public Location getMainLocationUp() {
        return mainLocationUp;
    }

    public Location getMainLocationDown() {
        return mainLocationDown;
    }

    public Location getSubLocationDown() {
        return subLocationDown;
    }

    public Location getSubLocationUp() {
        return subLocationUp;
    }

    /**
     * @return Arena对象信息是否完善
     */
    public boolean check() {
        return name!=null && mainLocationUp!=null && mainLocationDown!=null
                && subLocationUp!=null && subLocationDown!=null && chestSet.size()>=10 && mainTp!=null && subTp!=null;
    }

    public Set<Location> getChestSet() {
        return chestSet;
    }

    public String getName() {
        return name;
    }



    @Override
    public boolean equals(Object tar) {
        if (this == tar) return true;
        if (tar == null) return false;
        if (getClass() != tar.getClass()) return false;
        Arena tarArena = (Arena) tar;
        return name.equals(tarArena.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public Map<String,Object> serialize() {
        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        if (mainLocationDown!=null) map.put("mainLocationDown",mainLocationDown);
        if (mainLocationUp!=null) map.put("mainLocationUp",mainLocationUp);
        if (subLocationDown!=null) map.put("subLocationDown",subLocationDown);
        if (subLocationUp!=null) map.put("subLocationUp",subLocationUp);
        if (chestSet.size()>0) map.put("chestSet",chestSet);
        if (mainTp!=null) map.put("mainTp",mainTp);
        if (subTp!=null) map.put("subTp",subTp);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static Arena deserialize(Map<String,Object> map) {
        Arena arena = new Arena();
        arena.name = (String) map.get("name");
        if (map.containsKey("mainLocationDown")) arena.mainLocationDown = (Location) map.get("mainLocationDown");
        if (map.containsKey("mainLocationUp")) arena.mainLocationUp = (Location) map.get("mainLocationUp");
        if (map.containsKey("subLocationDown")) arena.subLocationDown = (Location) map.get("subLocationDown");
        if (map.containsKey("subLocationUp")) arena.subLocationUp = (Location) map.get("subLocationUp");
        if (map.containsKey("chestSet")) arena.chestSet = (Set<Location>) map.get("chestSet");
        if (map.containsKey("mainTp")) arena.setMainTp((Location) map.get("mainTp"));
        if (map.containsKey("subTp")) arena.setSubTp((Location) map.get("subTp"));
        return arena;
    }
}
