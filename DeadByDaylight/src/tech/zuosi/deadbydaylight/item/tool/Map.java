package tech.zuosi.deadbydaylight.item.tool;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.deadbydaylight.role.JerryRole;
import tech.zuosi.deadbydaylight.role.Role;
import tech.zuosi.deadbydaylight.type.ToolType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iwar on 2016/7/9.
 */
public class Map extends Tool {
    //����

    public Map() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a��ͼ"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a�鿴λ��"));
        super.setItem(new ItemStack(Material.MAP),"Map", lore);
        this.setToolType(ToolType.EFFECT);
    }

    @Override
    public boolean effect(Role role) {
        if (!(role instanceof JerryRole)) {
            return false;
        }
        JerryRole jerry = (JerryRole) role;
        //
        return false;
    }
}
