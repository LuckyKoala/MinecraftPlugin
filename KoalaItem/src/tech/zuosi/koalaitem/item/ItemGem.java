package tech.zuosi.koalaitem.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.type.GemType;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/18.
 */
public class ItemGem implements IItem {
    private ItemStack model = new ItemStack(Material.EMERALD);
    private GemType gemType;
    private int level;
    private ItemStack itemStack;

    public ItemGem() {
        this.gemType = GemType.EMPTY;
        this.itemStack = model;
        this.level = 0;
    }

    public ItemGem(GemType gemType) {
        this.gemType = gemType;
        this.itemStack = model;
        this.level = 0;
    }

    public ItemGem(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.gemType = GemType.valueOf((String)new NBTUtil(itemStack).getData("name"));
        this.level = (int)(new NBTUtil(itemStack).getData("gemLevel"));
    }

    @Override
    public ItemStack defaultItem() {
        return new NBTUtil(itemStack)
                .initData(gemType.name(), ItemType.GEM, new String[]{"0","0","0"})
                .setDisplayName(ChatColor.GREEN + gemType.getName()+"��ʯ")
                .initLore(new String[]{
                        ChatColor.GREEN + "�����ܡ�" + ChatColor.LIGHT_PURPLE + "����������������",
                        ChatColor.GREEN + "���÷���" + ChatColor.RED + "����Ʒһ��������̨",
                        ChatColor.GREEN + "���ȼ���" + ChatColor.RED + level
                })
                .getItemStack();
    }

    @Override
    public boolean canLevelUp() {
        return level <= 9;
    }

    @Override
    public boolean validate(ItemStack is) {
        NBTUtil util = new NBTUtil(is);
        Object type = util.getData("type");
        if (type == null) return false;
        return ((String) type).contains("GEM");
    }

    public int getLevel() {
        return this.level;
    }

    public GemType getGemType() {
        return gemType;
    }

    public ItemStack levelUp() {
        NBTUtil nbtUtil = new NBTUtil(itemStack);
        if (canLevelUp()) level++;
        nbtUtil.setData("gemLevel",level);
        return nbtUtil.initLore(new String[]{
                ChatColor.GOLD + "������Ƕ�Ը���������������",
                ChatColor.RED + "�ȼ�:" + level
        }).getItemStack();
    }
}
