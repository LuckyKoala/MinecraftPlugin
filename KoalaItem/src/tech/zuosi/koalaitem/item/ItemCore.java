package tech.zuosi.koalaitem.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.type.CoreType;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/18.
 */
public class ItemCore implements IItem {
    private ItemStack model;
    private CoreType coreType;

    public ItemCore() {}

    public ItemCore(ItemStack newModel) {
        this.model = newModel;
    }

    public ItemCore(int id) {
        if (id == 0) {
            model = new ItemStack(Material.MAGMA_CREAM);
            coreType = CoreType.FLAME;
        } else if (id == 1){
            model = new ItemStack(Material.EYE_OF_ENDER);
            coreType = CoreType.ENDERSIGNAL;
        }
    }

    public CoreType getCoreType() {
        return CoreType.valueOf((String)new NBTUtil(this.model).getData("name"));
    }

    @Override
    public ItemStack defaultItem() {
        return new NBTUtil(model)
                .initData(coreType.name(), ItemType.CORE,new String[]{"0","0","0"})
                .initLore(new String[]{
                        ChatColor.GREEN + "【功能】" + ChatColor.LIGHT_PURPLE + "为人物添加脚印特效",
                        ChatColor.GREEN + "【用法】" + ChatColor.RED + "转生嵌入武器"
                })
                .setDisplayName(ChatColor.GREEN + coreType.getName()+"核心")
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
        if (type == null) return false;
        return ((String) type).contains("CORE");
    }
}
