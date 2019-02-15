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
    //��
    public Saw() {
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a�˺�ֵ:2"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&a��������:1.5"));
        lore.add(ChatColor.translateAlternateColorCodes('&',"&e���Ҽ�������⣬��Ŀ�귢��ֱ�߳�̡�"));
        super.setItem(new ItemStack(Material.STONE_PICKAXE),"���",lore);
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
