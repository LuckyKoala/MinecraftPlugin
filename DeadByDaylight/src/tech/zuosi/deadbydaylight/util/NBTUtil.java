package tech.zuosi.deadbydaylight.util;

/**
 * Created by iwar on 2016/7/11.
 */

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;


/**
 * 0.获取版本
 * Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]
 * ---------------------------------------------
 * org.bukkit.craftbukkit.VERSION.CraftItemStack
 * net.minecraft.server.VERSION.NBTTagCompound
 * ---------------------------------------------
 * 设置NBT标签，具体步骤
 * 1.ItemStack转换为CraftItemStack（Bukkit的实现类）再调用
 * 方法转换为NMS ItemStack【asNMSCopy()】
 * 2.通过NMS ItemStack尝试获取物品的NBTTagCompound【getTag()】,
 * 如果为空，获取一个新的NBTTagCompound【反射获得类后实例化】
 * 3.根据数据类型调用NBTTagCompound的setter方法
 * （所有数据类型会被包装为NBT的数据类型,顶层为NBTBase类）
 * 4.通过反射调用方法，传递数据
 * 5.设置NMS ItemStack的NBTTagCompound为之前使用过的对象
 * 6.反射调用CraftItemStack的asCraftMirror方法，将NMS ItemStack对象
 * 转为CraftItemStack对象，之后强转为ItemStack对象，返回对象
 * ---------------------------------------------
 * 获取NBT标签，具体步骤
 * 1.ItemStack->NMS ItemStack
 * 2.通过NMS ItemStack尝试获取物品的NBTTagCompound【getTag()】,
 * 如果为空，获取一个新的NBTTagCompound【反射获得类后实例化】
 * 3.之后通过该对象的getter方法
 * ---------------------------------------------
 * 涉及反射
 * 1.Class forName() new Class[] {}
 * 2.Method invoke()
 * 3.Object
 */
public class NBTUtil {
    //TODO 考虑无法应用反射取得NBT时卸载插件（影响插件有关NBT的判定）
    //TODO NBTUtil是否必要？

    private final static String VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".",",").split(",")[3];

    public NBTUtil() {}

    private static Class getCraftItemStack() {
        try {
            return Class.forName("org.bukkit.craftbukkit." + VERSION + ".inventory.CraftItemStack");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("[黎明杀机插件]NBTUtil无法正常运行,可能不支持当前版本的服务端.");
            cnfe.printStackTrace();
            return null;
        }
    }

    private static Object asNMSCopy(ItemStack itemStack) {
        Class cis = getCraftItemStack();
        try {
            return cis.getMethod("asNMSCopy",ItemStack.class).invoke(cis, itemStack);
        } catch (Exception e) {
            System.out.println("[黎明杀机插件]NBTUtil无法正常运行,可能不支持当前版本的服务端.");
            e.printStackTrace();
            return null;
        }
    }

    private static ItemStack asCraftMirror(Object nmsitem) {
        Class cis = getCraftItemStack();
        try {
            return (ItemStack)cis.getMethod("asCraftMirror",nmsitem.getClass())
                    .invoke(cis, nmsitem);
        } catch (Exception e) {
            System.out.println("[黎明杀机插件]NBTUtil无法正常运行,可能不支持当前版本的服务端.");
            e.printStackTrace();
            return null;
        }
    }

    private static Object getNewTagCompound() {
        try {
            return Class.forName("net.minecraft.server." + VERSION + ".NBTTagCompound").newInstance();
        } catch (Exception e) {
            System.out.println("[黎明杀机插件]NBTUtil无法正常运行,可能不支持当前版本的服务端.");
            e.printStackTrace();
            return null;
        }
    }

    private static Object getTagCompound(Object nmsitem) {
        try {
            return nmsitem.getClass().getMethod("getTag",new Class[0]).invoke(nmsitem);
        } catch (Exception e) {
            System.out.println("[黎明杀机插件]NBTUtil无法正常运行,可能不支持当前版本的服务端.");
            e.printStackTrace();
            return null;
        }
    }

    private static Object setTagCompound(Object tagcompound,Object nmsitem) {
        try {
            nmsitem.getClass().getMethod("setTag",tagcompound.getClass()).invoke("setTag",tagcompound);
            return nmsitem;
        } catch (Exception e) {
            System.out.println("[黎明杀机插件]NBTUtil无法正常运行,可能不支持当前版本的服务端.");
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack setInt(ItemStack is,String key,int value) {
        Object nmsitem = asNMSCopy(is);
        if (nmsitem == null) {
            System.out.println("[黎明杀机插件]NBTUtil无法正常运行,可能不支持当前版本的服务端");
            return null;
        } else {
            Object tagcompound = getTagCompound(nmsitem);
            if (tagcompound == null) {
                tagcompound = getNewTagCompound();
            }
            try {
                tagcompound.getClass().getMethod("setInt",String.class,int.class)
                        .invoke(tagcompound, key,value);
                return asCraftMirror(setTagCompound(tagcompound,nmsitem));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    public static Integer getInt(ItemStack is,String key) {
        Object nmsitem = asNMSCopy(is);
        if (nmsitem == null) {
            System.out.println("[黎明杀机插件]NBTUtil无法正常运行,可能不支持当前版本的服务端");
            return null;
        } else {
            Object tagcompound = getTagCompound(nmsitem);
            if (tagcompound == null) {
                tagcompound = getNewTagCompound();
            }
            try {
                return (Integer) tagcompound.getClass().getMethod("getInt",String.class)
                        .invoke(tagcompound, key);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public static ItemStack setBoolean(ItemStack is,String key,boolean value) {
        Object nmsitem = asNMSCopy(is);
        if (nmsitem == null) {
            System.out.println("[黎明杀机插件]NBTUtil无法正常运行,可能不支持当前版本的服务端");
            return null;
        } else {
            Object tagcompound = getTagCompound(nmsitem);
            if (tagcompound == null) {
                tagcompound = getNewTagCompound();
            }
            try {
                tagcompound.getClass().getMethod("setBoolean",String.class,boolean.class)
                        .invoke(tagcompound, key,value);
                return asCraftMirror(setTagCompound(tagcompound,nmsitem));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
    public static Boolean getBoolean(ItemStack is,String key) {
        Object nmsitem = asNMSCopy(is);
        if (nmsitem == null) {
            System.out.println("[黎明杀机插件]NBTUtil无法正常运行,可能不支持当前版本的服务端");
            return null;
        } else {
            Object tagcompound = getTagCompound(nmsitem);
            if (tagcompound == null) {
                tagcompound = getNewTagCompound();
            }
            try {
                return (Boolean) tagcompound.getClass().getMethod("getBoolean",boolean.class)
                        .invoke(tagcompound, key);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
