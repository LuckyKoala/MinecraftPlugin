package tech.zuosi.koalaitem.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/18.
 */
public class ItemRemove implements IItem {
    private ItemStack model = new ItemStack(Material.SHEARS);

    public ItemRemove() {}

    @Override
    public ItemStack defaultItem() {
        return new NBTUtil(model)
                .initData("�ɼ���", ItemType.ITEM,new String[]{"0","0","0"})
                .initLore(new String[]{
                        ChatColor.GOLD + "������Ϊ���ߣ��ɼ�����������Ƕ�ı�ʯ.",
                        ChatColor.RED + "һ���ɼ���ֻ�ܲɼ�һ�ű�ʯ��֮������ʧ"
                })
                .setDisplayName("�ɼ���")
                .getItemStack();
    }

    @Override
    public boolean canLevelUp() {
        return false;
    }

    @Override
    public boolean validate(ItemStack is) {
        NBTUtil util = new NBTUtil(is);
        Object type = util.getData("type");
        Object name = util.getData("name");
        if (type == null || name == null) return false;
        return ((String) type).contains("ITEM") && ((String) name).contains("�ɼ���");
    }
}
