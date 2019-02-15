package tech.zuosi.deadbydaylight.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import tech.zuosi.deadbydaylight.data.FurnaceProperty;
import tech.zuosi.deadbydaylight.data.RoleMap;
import tech.zuosi.deadbydaylight.data.StructureMap;
import tech.zuosi.deadbydaylight.role.JerryRole;
import tech.zuosi.deadbydaylight.role.Role;

/**
 * Created by iwar on 2016/7/9.
 */
public class FurnaceOpenListener {
    private RoleMap roleMap = new RoleMap();
    private StructureMap structureMap = new StructureMap();

    public FurnaceOpenListener() {}

    @EventHandler
    public void onFurnaceOpen(PlayerInteractEvent pie) {
        Block block = pie.getClickedBlock();
        if (block == null) return;
        //BurningFurnace?
        if (Material.FURNACE == block.getType()) {
            Role role = roleMap.queryRole(pie.getPlayer());
            if (role == null) return;
            if (!(role instanceof JerryRole)) return;
            FurnaceProperty furnaceProperty = structureMap.queryFurnaceProperty(block.getLocation());
            int manCounter = furnaceProperty.getBURN_TIME();
            int progress = furnaceProperty.getCOOK_TIME();
            if (manCounter != 100) {
                furnaceProperty.BURN_TIME(manCounter+25);
            }
            //TODO 进一步完善
        }
    }
}
