package net.myplugin.visualintensify.util;


import me.dpohvar.powernbt.PowerNBT;
import me.dpohvar.powernbt.api.NBTCompound;
import me.dpohvar.powernbt.api.NBTList;
import me.dpohvar.powernbt.api.NBTManager;
import org.bukkit.inventory.ItemStack;

/**
 * Created by iwar on 2016/7/16.
 */
public class NBTUtil {
    private static NBTManager nbtManager;

    static {
        nbtManager = PowerNBT.getApi();
    }

    public NBTUtil() {}

    public static void rewriteLore(ItemStack itemStack,String[] args) {
        int SIZE = args.length;
        NBTCompound nbtCompound = nbtManager.read(itemStack);
        NBTList lore = new NBTList();
        for (String arg:args) {
            lore.add(arg);
        }
        nbtCompound.put("Lore",lore);
        nbtManager.write(itemStack,nbtCompound);
    }

    public static void writeMenu(ItemStack itemStack,String displayName) {
        NBTCompound nbtCompound = nbtManager.read(itemStack);
        nbtCompound.put("viMenu",true);
        nbtCompound.put("Name",displayName);
        nbtManager.write(itemStack,nbtCompound);
    }
    public static boolean isMenu(ItemStack itemStack) {
        NBTCompound nbtCompound = nbtManager.read(itemStack);
        return nbtCompound.getBoolean("viMenu");
    }

    public static void writeItem(ItemStack itemStack,String displayName,String typeName) {
        NBTCompound nbtCompound = nbtManager.read(itemStack);
        nbtCompound.put("viItem",true);
        nbtCompound.put("Name",displayName);
        nbtCompound.put("typeName",typeName);
        nbtManager.write(itemStack,nbtCompound);
    }
    public static boolean isCorrectItem(ItemStack itemStack,String typeName) {
        NBTCompound nbtCompound = nbtManager.read(itemStack);
        return nbtCompound.getBoolean("viItem") && typeName.equals(nbtCompound.getString("typeName"));
    }


}
