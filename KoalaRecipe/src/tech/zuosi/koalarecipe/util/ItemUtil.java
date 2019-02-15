package tech.zuosi.koalarecipe.util;

import me.dpohvar.powernbt.PowerNBT;
import me.dpohvar.powernbt.api.NBTCompound;
import me.dpohvar.powernbt.api.NBTManager;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalarecipe.KoalaRecipe;

/**
 * Created by iwar on 2016/8/27.
 */
public class ItemUtil {
    private ItemStack itemStack;
    private static final NBTManager NBT_MANAGER = PowerNBT.getApi();

    public ItemUtil(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        ItemUtil iu = (ItemUtil) o;
        if (!this.getItemStack().toString().equals(iu.getItemStack().toString())) {
            return false;
        }
        if (Material.AIR == this.getItemStack().getType()) {
            return Material.AIR == iu.getItemStack().getType();
        }
        NBTCompound former = NBT_MANAGER.read(this.getItemStack());
        NBTCompound latter = NBT_MANAGER.read(iu.getItemStack());

        if (former == null) {
            return latter==null;
        }

        boolean result = former.toString().equals(latter.toString());
        if (KoalaRecipe.onDebug) {
            System.out.println("[FormerNBT] " + former.toString());
            System.out.println("[LatterNBT] " + latter.toString());
            System.out.println("[Result]    " + result);
        }
        return result;
    }

    @Override
    public int hashCode() {
        return NBT_MANAGER.read(this.getItemStack()).hashCode();
    }
}
