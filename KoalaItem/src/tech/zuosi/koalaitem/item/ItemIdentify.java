package tech.zuosi.koalaitem.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/18.
 */
public class ItemIdentify implements IItem {
    private ItemStack model = new ItemStack(Material.DIAMOND);

    public ItemIdentify() {}

    @Override
    public ItemStack defaultItem() {
        return new NBTUtil(model)
                .initData("����ʯ", ItemType.ITEM,new String[]{"0","0","0"})
                .initLore(new String[]{
                        ChatColor.GREEN + "�����ܡ�" + ChatColor.LIGHT_PURPLE + "��������",
                        ChatColor.GREEN + "���÷���" + ChatColor.RED + "����Ʒһ��������̨"
                })
                .setDisplayName(ChatColor.GREEN + "����ʯ")
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
        return ((String) type).contains("ITEM") && ((String) name).contains("����ʯ");
    }
}
