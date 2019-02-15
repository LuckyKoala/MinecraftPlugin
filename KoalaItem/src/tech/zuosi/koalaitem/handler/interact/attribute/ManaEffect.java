package tech.zuosi.koalaitem.handler.interact.attribute;

import com.herocraftonline.heroes.api.events.HeroRegainManaEvent;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/24.
 */
public class ManaEffect implements Listener {

    @EventHandler
    public void onRegainMana(HeroRegainManaEvent hrme) {
        int amount = hrme.getAmount();
        Player player = hrme.getHero().getPlayer();
        ItemStack is = player.getItemInHand();
        if (!shouldExecute(is)) return;
        NBTUtil util = new NBTUtil(is);

        int MGA = (int)util.getAttribute("MGA");
        double factor = KoalaItem.INSTANCE.getConfig().getDouble("Attribute.MGA.regenFactor");
        amount *= 1.0D+factor;
        hrme.setAmount(amount);
    }

    public boolean shouldExecute(ItemStack is) {
        is = is==null?new ItemStack(Material.AIR):is;
        if (Material.AIR == is.getType()) return false;
        NBTUtil util = new NBTUtil(is);
        Object o = util.getData("type");
        if (o == null) return false;
        return ItemType.PLAYERITEM == ItemType.valueOf((String) o);
    }
}
