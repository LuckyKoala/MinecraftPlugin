package tech.zuosi.deadbydaylight.item.tool;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.zuosi.deadbydaylight.item.GameItem;
import tech.zuosi.deadbydaylight.role.Role;
import tech.zuosi.deadbydaylight.type.ToolType;

import java.util.List;

/**
 * Created by iwar on 2016/7/8.
 */
public abstract class Tool implements GameItem {
    private ToolType toolType;
    private ItemStack modelItem;

    public Tool() {
        this.toolType = ToolType.OTHER;
        this.setItem(new ItemStack(Material.PAPER),"Unknown",null);
    }

    @Override
    public boolean getItem(Player player) {
        if (player.isOnline() && !player.isDead()) {
            player.getWorld().dropItem(player.getLocation(),this.modelItem);
            return true;
        }
        return false;
    }
    @Override
    public void setItem(ItemStack modelItem,String displayName,List<String> lore) {
        ItemMeta im = modelItem.getItemMeta();
        im.setLore(lore);
        im.setDisplayName(displayName);
        modelItem.setItemMeta(im);
        this.modelItem = modelItem;
    }

    public ToolType getToolType() {
        return toolType;
    }
    public void setToolType(ToolType toolType) {
        this.toolType = toolType;
    }

    public abstract boolean effect(Role role);
}
