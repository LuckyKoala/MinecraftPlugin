package tech.zuosi.deadbydaylight.item.tool;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.deadbydaylight.role.JerryRole;
import tech.zuosi.deadbydaylight.role.Role;
import tech.zuosi.deadbydaylight.type.DamageCause;
import tech.zuosi.deadbydaylight.type.ToolType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iwar on 2016/7/8.
 */
public class Trap extends Tool {
    private DamageCause damageCause;

    public Trap() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a陷阱"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a放置后可以困住踩中夹子的人类"));
        super.setItem(new ItemStack(Material.CARPET),"Trap", lore);
        this.setToolType(ToolType.DANGER);
        this.damageCause = DamageCause.TRAP;
    }

    @Override
    public boolean effect(Role role) {
        if (!(role instanceof JerryRole)) {
            return false;
        }
        JerryRole jerry = (JerryRole) role;
        int hurtCount = jerry.getHurtCount();
        if (hurtCount >= 2) {
            return false;
        }
        jerry.setHurtCount(hurtCount+1);
        jerry.echo(ChatColor.RED + "踩中夹子将使附近的屠夫感知到你，请尽快离开这里。");
        return true;
    }
}
