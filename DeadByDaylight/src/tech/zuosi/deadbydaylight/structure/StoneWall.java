package tech.zuosi.deadbydaylight.structure;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * Created by iwar on 2016/7/9.
 */
public class StoneWall implements GameStructure {

    public StoneWall() {}

    @Override
    public void build(Location location) {
        Block center,east,west;

        center = location.getBlock().getRelative(BlockFace.UP);
        east = center.getRelative(BlockFace.EAST);
        west = center.getRelative(BlockFace.WEST);

        center.setType(Material.STONE);
        center.getRelative(BlockFace.NORTH).setType(Material.LADDER,false);
        center.getRelative(BlockFace.SOUTH).setType(Material.LADDER,false);
        center.getRelative(BlockFace.UP).setType(Material.STONE_SLAB2);
        east.setType(Material.STONE);
        west.setType(Material.STONE);
        east.getRelative(BlockFace.UP).setType(Material.STONE);
        east.getRelative(BlockFace.UP,2).setType(Material.STONE);
        west.getRelative(BlockFace.UP).setType(Material.STONE);
        west.getRelative(BlockFace.UP,2).setType(Material.STONE);
    }
}
