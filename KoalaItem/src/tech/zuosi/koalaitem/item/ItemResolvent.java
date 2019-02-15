package tech.zuosi.koalaitem.item;

import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.util.NBTOperator;
import tech.zuosi.koalaitem.util.NBTUtil;

/**
 * Created by iwar on 2016/7/18.
 */
public class ItemResolvent implements IItem {
    private ItemStack model;

    public ItemResolvent() {}

    public ItemResolvent(ItemStack itemStack) {
        this.model = itemStack;
    }

    @Override
    public ItemStack defaultItem() {
        return new NBTOperator(new NBTUtil(model)).extract().getItemStack();
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
        return ((String) type).contains("RESOLVENT");
    }
}
