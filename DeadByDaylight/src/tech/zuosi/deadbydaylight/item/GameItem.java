package tech.zuosi.deadbydaylight.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * Created by iwar on 2016/7/9.
 */
public interface GameItem {
    boolean getItem(Player player);
    void setItem(ItemStack modelItem,String displayName,List<String> lore);
}
