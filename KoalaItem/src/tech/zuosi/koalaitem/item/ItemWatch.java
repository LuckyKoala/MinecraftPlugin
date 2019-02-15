package tech.zuosi.koalaitem.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/23.
 */
public class ItemWatch implements IItem {
    private static ItemStack clock = new ItemStack(Material.WATCH);

    static {
        clock = new NBTUtil(clock)
                .initData("ShowMenu", ItemType.MENU,new String[]{"0","0","0"})
                .setDisplayName(ChatColor.YELLOW + "KI�˵�")
                .initLore(new String[]{
                        ChatColor.GREEN + "�����ܡ�" + ChatColor.LIGHT_PURPLE + "չʾKI�˵�",
                        ChatColor.GREEN + "���÷���" + ChatColor.RED + "�ֳ���Ʒ�Ҽ�"
                })
                .getItemStack();
    }


    @Override
    public ItemStack defaultItem() {
        return clock;
    }

    @Override
    public boolean canLevelUp() {
        return false;
    }

    @Override
    public boolean validate(ItemStack is) {
        NBTUtil util = new NBTUtil(is);
        Object name = util.getData("name");
        if (name == null) return false;
        return "ShowMenu".equalsIgnoreCase((String)name);
    }
}
