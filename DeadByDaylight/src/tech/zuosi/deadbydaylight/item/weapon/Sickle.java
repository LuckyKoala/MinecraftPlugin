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
public class Sickle extends Weapon {
    //µ∂
    public Sickle() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a…À∫¶÷µ:1"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&aπ•ª˜æ‡¿Î:2.0"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&eπ•ª˜æ‡¿ÎΩœ‘∂"));
        super.setItem(new ItemStack(Material.STONE_HOE),"¡≠µ∂",lore);
        super.setAttackValue(1);
        super.setAttackDistance(2.0);
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
