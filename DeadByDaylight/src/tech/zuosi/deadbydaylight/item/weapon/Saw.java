package tech.zuosi.deadbydaylight.item.weapon;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iwar on 2016/7/8.
 */
public class Saw extends Weapon {
    //锯
    public Saw() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a伤害值:2"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a攻击距离:1.5"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&e按右键启动电锯，对目标发起直线冲刺。"));
        super.setItem(new ItemStack(Material.STONE_PICKAXE),"电锯",lore);
        super.setAttackValue(2);
        super.setAttackDistance(1.5);
    }

    @Override
    public boolean tryAttack(Location killerLocation,Location victimLocation) {
        return super.tryAttack(killerLocation,victimLocation);
    }

    @Override
    public boolean getItem(Player player) {
        return super.getItem(player);
    }
}
