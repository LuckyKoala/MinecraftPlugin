package tech.zuosi.koalaitem.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.type.CoreType;
import tech.zuosi.koalaitem.type.ItemType;

/**
 * Created by iwar on 2016/7/29.
 */
public class CoreUtil {

    public CoreUtil() {}

    public boolean shouldExecute(ItemStack is) {
        is = is==null?new ItemStack(Material.AIR):is;
        if (Material.AIR == is.getType()) return false;
        NBTUtil util = new NBTUtil(is);
        Object o = util.getData("type");
        if (o == null) return false;
        if (ItemType.PLAYERITEM == ItemType.valueOf((String) o)) {
            CoreType coreType = getCoreType(util);
            return CoreType.EMPTY != coreType;
        }
        return false;
    }

    public CoreType getCoreType(NBTUtil util) {
        return CoreType.valueOf((String)util.getItemData("coreType"));
    }
}
