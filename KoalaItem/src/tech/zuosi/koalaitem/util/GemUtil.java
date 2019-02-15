package tech.zuosi.koalaitem.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.type.GemType;
import tech.zuosi.koalaitem.type.ItemType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/7/28.
 */
public class GemUtil {

    public GemUtil() {}

    public boolean shouldExecute(ItemStack is) {
        is = is==null?new ItemStack(Material.AIR):is;
        if (Material.AIR == is.getType()) return false;
        NBTUtil util = new NBTUtil(is);
        Object o = util.getData("type");
        if (o == null) return false;
        if (ItemType.PLAYERITEM == ItemType.valueOf((String) o)) {
            GemType one = GemType.valueOf((String)util.getItemData("gemOne"));
            GemType two = GemType.valueOf((String)util.getItemData("gemTwo"));
            GemType three = GemType.valueOf((String)util.getItemData("gemThree"));
            return !((GemType.VOID == one || GemType.EMPTY == one)
                    && (GemType.VOID == two || GemType.EMPTY == two)
                    && (GemType.VOID == three || GemType.EMPTY == three));
        }
        return false;
    }

    public int getGemLevel(NBTUtil util,GemType gemType) {
        if (getGemMap(util).containsKey(gemType)) {
            return getGemMap(util).get(gemType);
        }
        return -1;
    }

    private Map<GemType,Integer> getGemMap(NBTUtil util) {
        GemType one = GemType.valueOf((String)util.getItemData("gemOne"));
        int oneLevel = (int)util.getItemData("gemOneLevel");
        GemType two = GemType.valueOf((String)util.getItemData("gemTwo"));
        int twoLevel = (int)util.getItemData("gemTwoLevel");
        GemType three = GemType.valueOf((String)util.getItemData("gemThree"));
        int threeLevel = (int)util.getItemData("gemThreeLevel");
        Map<GemType,Integer> gemFactorMap = new HashMap<>();

        gemFactorMap.put(one,oneLevel);
        gemFactorMap.put(two,twoLevel);
        gemFactorMap.put(three,threeLevel);

        return gemFactorMap;
    }
}
