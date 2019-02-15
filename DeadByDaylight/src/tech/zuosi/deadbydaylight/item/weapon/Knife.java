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
public class Knife extends Weapon {
    //С��
    public Knife() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a�˺�ֵ:1"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a��������:0.8"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&e��������϶̵�С����"));
        super.setItem(new ItemStack(Material.STONE_PICKAXE),"���",lore);
        super.setAttackValue(1);
        super.setAttackDistance(0.7);
    }

    @Override
    public boolean tryAttack(Location killerLocation, Location victimLocation) {
        return super.tryAttack(killerLocation,victimLocation);
    }

    @Override
    public boolean getItem(Player player) {
        return super.getItem(player);
    }
}
