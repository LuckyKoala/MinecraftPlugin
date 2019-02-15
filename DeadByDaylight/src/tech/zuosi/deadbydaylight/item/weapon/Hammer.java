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
public class Hammer extends Weapon {
    //´¸
    public Hammer() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&',"&aÉËº¦Öµ:1"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a¹¥»÷¾àÀë:1.0"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&eÆÕÍ¨µÄ´¸×Ó¡£"));
        super.setItem(new ItemStack(Material.STONE_AXE),"Ê¯´¸",lore);
        super.setAttackValue(1);
        super.setAttackDistance(1.0);
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
