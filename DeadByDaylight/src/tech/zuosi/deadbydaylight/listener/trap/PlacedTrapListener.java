package tech.zuosi.deadbydaylight.listener.trap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import tech.zuosi.deadbydaylight.data.RoleMap;
import tech.zuosi.deadbydaylight.data.StructureMap;
import tech.zuosi.deadbydaylight.item.tool.Trap;
import tech.zuosi.deadbydaylight.role.JerryRole;
import tech.zuosi.deadbydaylight.role.Killer;
import tech.zuosi.deadbydaylight.role.Role;
import tech.zuosi.deadbydaylight.structure.PlacedTrap;

/**
 * Created by iwar on 2016/7/10.
 */
public class PlacedTrapListener implements Listener {
    private Trap trap = new Trap();
    private RoleMap roleMap = new RoleMap();
    private PlacedTrap placedTrap = new PlacedTrap();
    private StructureMap structureMap = new StructureMap();

    @EventHandler
    public void onSteppingOnto(PlayerInteractEvent pie) {
        if (Action.PHYSICAL != pie.getAction()) return;
        Block block = pie.getPlayer().getLocation().getBlock();
        if (block == null) return;
        if (Material.CARPET == block.getType()) {
            Role role = roleMap.queryRole(pie.getPlayer());
            if (role == null) return;
            if (!(role instanceof JerryRole)) return;
            JerryRole jerry = (JerryRole) role;
            if (trap.effect(jerry)) {  //致伤
                placedTrap.build(block.getRelative(BlockFace.DOWN).getLocation()); //困住
            }
        }
    }

    @EventHandler
    public void onKillerInteract(PlayerInteractEvent pie) {
        Block block = pie.getClickedBlock();
        if (block == null) return;
        if (Material.FENCE == block.getType()) {
            Role role = roleMap.queryRole(pie.getPlayer());
            if (role == null) return;
            if (!(role instanceof Killer)) return;
            Killer killer = (Killer) role;
            Location trapLocation = structureMap.queryTrapLocation(block.getLocation());
            if (trapLocation == null) return;
            placedTrap.delete(trapLocation);
            killer.echo(ChatColor.GREEN + "已解除夹子");
        }
    }
}
