package tech.zuosi.deadbydaylight.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import tech.zuosi.deadbydaylight.data.RoleMap;
import tech.zuosi.deadbydaylight.data.StructureMap;
import tech.zuosi.deadbydaylight.role.JerryRole;
import tech.zuosi.deadbydaylight.role.Role;

/**
 * Created by iwar on 2016/7/9.
 */
public class GenerateDoorListener implements Listener {
    private RoleMap roleMap = new RoleMap();
    private StructureMap structureMap = new StructureMap();
    //FIXME 这个类可能没什么卵用


    public GenerateDoorListener() {}

    @EventHandler
    public void onRightClick(PlayerInteractEvent pie) {
        Block block = pie.getClickedBlock();
        if (block == null) return;
        if (Material.STONE_BUTTON == block.getType()) {
            Role role = roleMap.queryRole(pie.getPlayer());
            if (role == null) return;
            if (!(role instanceof JerryRole)) return;
            Location location = structureMap.queryDoorGenerateLocation(block.getLocation());
            if (location == null) {
                //报错
                return;
            }
            location.getBlock().setType(Material.IRON_DOOR);
            role.echo(ChatColor.GREEN + "成功布置路障");
        }
    }
}
