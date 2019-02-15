package tech.zuosi.deadbydaylight.item.weapon;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.zuosi.deadbydaylight.item.GameItem;

import java.util.List;

/**
 * Created by iwar on 2016/7/8.
 */
public class Weapon implements GameItem {
    private int attackValue;
    private ItemStack modelItem;
    private double attackDistance;

    public Weapon() {
        this.attackValue = 1;
        this.attackDistance = 1.0;
        this.setItem(new ItemStack(Material.PAPER),"Unknown",null);
    }

    public boolean tryAttack(Location killerLocation, Location victimLocation) {
        return killerLocation.distance(victimLocation) <= this.getAttackDistance();
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

    public int getAttackValue() {
        return attackValue;
    }
    public void setAttackValue(int attackValue) {
        this.attackValue = attackValue;
    }
    public double getAttackDistance() {
        return attackDistance;
    }
    public void setAttackDistance(double attackDistance) {
        this.attackDistance = attackDistance;
    }
}
