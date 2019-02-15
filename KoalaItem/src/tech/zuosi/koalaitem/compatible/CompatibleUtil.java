package tech.zuosi.koalaitem.compatible;

import me.dpohvar.powernbt.api.NBTList;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.item.*;
import tech.zuosi.koalaitem.type.CoreType;
import tech.zuosi.koalaitem.type.GemType;
import tech.zuosi.koalaitem.type.ItemQuality;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/16.
 */
public class CompatibleUtil {
    //TODO ��һ�����Լ��ݣ�����ȥ��VI�����ʵ�ָ����Ƶļ���
    public ItemStack asKICopy(ItemStack item) {
        item = item==null?new ItemStack(Material.AIR):item;
        if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
            NBTUtil util = new NBTUtil(item);
            NBTList lore = util.getDisplayLore();
            Material itemType = item.getType();
            int size = lore.size();
            String str = (String)lore.get(0);

            if (size == 8) {
                if (str.equals("vi:�û���Ʒ")) {
                    int intensifyLevel = Integer.parseInt(((String)lore.get(2)).split(":")[1]);
                    int rebornLevel = Integer.parseInt(((String)lore.get(3)).split(":")[1]);
                    String itemQualityName = ItemQuality.NORMAL.getTypeName(rebornLevel);
                    GemType gemType = GemType.VOID.nameToType(((String)lore.get(4)).split(":")[1]);
                    CoreType coreType = CoreType.EMPTY.nameToType(((String)lore.get(5)).split(":")[1]);

                    util = util.initData("bala", ItemType.PLAYERITEM, new String[]{"0","0","0"});
                    util.setItemData("quality",itemQualityName);
                    util.setItemData("intensifyLevel",intensifyLevel);
                    util.setItemData("rebornLevel",rebornLevel);
                    util.setItemData("gemOne",gemType.name());
                    util.setItemData("coreType",coreType.name());
                    return util.updateDisplayData().getItemStack();
                }
            } else if (size == 1) {
                if (!viValidate(str)) return item;
                if ("vi:ǿ�����".equalsIgnoreCase(str)) {
                    if (Material.WATCH == itemType) {
                        return new ItemWatch().defaultItem();
                    }
                } else if (Material.EMERALD == itemType) {
                    return new ItemGem(GemType.VOID.nameToType(getTypeName(str))).defaultItem();
                }
            } else if (size == 2) {
                if (!viValidate(str)) return item;
                if (Material.SHEARS == itemType) {
                    return new ItemStack(Material.SHEARS);
                } else if (Material.MAGMA_CREAM == itemType) {
                    return new ItemCore(0).defaultItem();
                } else if (Material.EYE_OF_ENDER == itemType) {
                    return new ItemCore(1).defaultItem();
                } else if (Material.NETHER_STAR == itemType) {
                    return new ItemReborn().defaultItem();
                } else if (Material.PAPER == itemType) {
                    return new ItemSafetyIntensify().defaultItem();
                } else if (Material.EMERALD == itemType) {
                    String a = "vi:" + "��dǿ��ʯ";
                    String b = "vi:" + "��d����ϵǿ��ʯ";
                    if (a.equalsIgnoreCase(str)) {
                        return new ItemIntensify().defaultItem();
                    } else if (b.equalsIgnoreCase(str)) {
                        return new ItemLuckyIntensify().defaultItem();
                    }
                }
            }
        }
        return item;
    }

    public boolean convertConfig() {
        //ת���ɰ������ļ�
        return false;
    }

    private String getTypeName(String str) {
        return str.split("-")[1];
    }

    private boolean viValidate(String str) {
        return str.contains("vi:");
    }
}
