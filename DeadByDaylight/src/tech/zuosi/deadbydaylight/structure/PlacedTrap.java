package tech.zuosi.deadbydaylight.structure;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import tech.zuosi.deadbydaylight.data.StructureMap;

/**
 * Created by iwar on 2016/7/9.
 */
public class PlacedTrap implements GameStructure {
    private StructureMap structureMap = new StructureMap();

    public PlacedTrap() {}

    public void place(Location location) {
        Block center = location.getBlock().getRelative(BlockFace.UP);
        center.setType(Material.HOPPER);
    }

    public void set(Location location) {
        Block center = location.getBlock().getRelative(BlockFace.UP);
        center.setType(Material.CARPET);
        structureMap.storeTrapLocation(center.getLocation());
    }

    @Override
    public void build(Location location) {
        Block center;
        center = location.getBlock();
        if (Material.CARPET == center.getType()) {
            center.setType(Material.AIR);

            center.getRelative(BlockFace.NORTH).setType(Material.FENCE);
            center.getRelative(BlockFace.NORTH_EAST).setType(Material.FENCE);
            center.getRelative(BlockFace.NORTH_WEST).setType(Material.FENCE);

            center.getRelative(BlockFace.EAST).setType(Material.FENCE);
            center.getRelative(BlockFace.WEST).setType(Material.FENCE);

            center.getRelative(BlockFace.SOUTH).setType(Material.FENCE);
            center.getRelative(BlockFace.SOUTH_EAST).setType(Material.FENCE);
            center.getRelative(BlockFace.SOUTH_WEST).setType(Material.FENCE);
        }
    }

    public boolean delete(Location location) {
        Block center;
        center = location.getBlock();
        if (Material.CARPET == center.getType()) {
            center.getRelative(BlockFace.NORTH).setType(Material.AIR);
            center.getRelative(BlockFace.NORTH_EAST).setType(Material.AIR);
            center.getRelative(BlockFace.NORTH_WEST).setType(Material.AIR);

            center.getRelative(BlockFace.EAST).setType(Material.AIR);
            center.getRelative(BlockFace.WEST).setType(Material.AIR);

            center.getRelative(BlockFace.SOUTH).setType(Material.AIR);
            center.getRelative(BlockFace.SOUTH_EAST).setType(Material.AIR);
            center.getRelative(BlockFace.SOUTH_WEST).setType(Material.AIR);

            structureMap.removeTrapLocation(center.getLocation());
            return true;
        }
        return false;
    }
}
