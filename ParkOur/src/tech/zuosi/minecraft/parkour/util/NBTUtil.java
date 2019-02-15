package tech.zuosi.minecraft.parkour.util;

import lombok.Getter;
import lombok.Setter;
import me.dpohvar.powernbt.PowerNBT;
import me.dpohvar.powernbt.api.NBTCompound;
import me.dpohvar.powernbt.api.NBTList;
import me.dpohvar.powernbt.api.NBTManager;
import org.bukkit.inventory.ItemStack;

/**
 * Created by LuckyKoala on 18-9-17.
 */
public class NBTUtil {
    static {
        manager = PowerNBT.getApi();
    }

    private static final NBTManager manager;
    private NBTCompound compound;
    @Getter
    @Setter
    private ItemStack itemStack;

    public NBTUtil(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.compound = manager.read(itemStack);
        if (compound == null) this.compound = new NBTCompound();
    }

    public NBTList getDisplayLore() {
        NBTCompound display = getDisplayCompound();
        return display.getList("Lore")==null?new NBTList():display.getList("Lore");
    }

    public NBTUtil setDisplayName(String displayName) {
        NBTCompound display = getDisplayCompound();
        display.put("Name",displayName);
        compound.put("display",display);
        manager.write(itemStack,compound);

        return this;
    }

    public NBTUtil initLore(String[] description) {
        rewriteLore(description);
        return this;
    }

    public NBTUtil setData(String key, Object value) {
        NBTCompound data = compound.compound("POData");
        data.put(key, value);
        compound.put("POData", data);
        manager.write(itemStack,compound);

        return this;
    }

    public Object getData(String key) {
        if (!compound.containsKey("POData")) {
            return null;
        }
        NBTCompound data = compound.compound("POData");
        return data.get(key);
    }

    //DisplayUtil
    private NBTCompound getDisplayCompound() {
        return compound.getCompound("display")==null?new NBTCompound():compound.getCompound("display");
    }

    private void rewriteLore(String[] args) {
        NBTList lore = new NBTList(args);
        NBTCompound display = getDisplayCompound();
        display.put("Lore",lore);
        compound.put("display",display);
        manager.write(itemStack,compound);
    }
}
