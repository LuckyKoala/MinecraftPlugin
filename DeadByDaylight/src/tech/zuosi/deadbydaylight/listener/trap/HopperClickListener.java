package tech.zuosi.deadbydaylight.listener.trap;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import tech.zuosi.deadbydaylight.data.RoleMap;
import tech.zuosi.deadbydaylight.data.StructureMap;
import tech.zuosi.deadbydaylight.item.tool.Trap;
import tech.zuosi.deadbydaylight.role.Killer;
import tech.zuosi.deadbydaylight.role.Role;

/**
 * Created by iwar on 2016/7/10.
 */
public class HopperClickListener implements Listener {
    private RoleMap roleMap = new RoleMap();
    private StructureMap structureMap = new StructureMap();

    @EventHandler
    public void onClick(PlayerInteractEvent pie) {
        Block block = pie.getClickedBlock();
        if (block == null) return;
        //TODO CLICK 点击类型 左键还是右键分开处理？
        if (Material.HOPPER == block.getType()) {
            Role role = roleMap.queryRole(pie.getPlayer());
            if (role == null) return;
            if (!(role instanceof Killer)) return;
            Killer killer = (Killer) role;
            new Trap().getItem(pie.getPlayer());
        }
    }
}
