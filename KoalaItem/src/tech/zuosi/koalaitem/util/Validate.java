package tech.zuosi.koalaitem.util;

import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.type.ItemType;

/**
 * Created by iwar on 2016/7/18.
 */
public class Validate {
    public static boolean isOperateLegal(NBTUtil util) {
        return KoalaItem.INSTANCE.getConfig().getBoolean("List."+util.getItemStack().getType().name())
                && util.getData("type") != null
                && ItemType.PLAYERITEM == ItemType.valueOf((String) util.getData("type"));
    }
}
