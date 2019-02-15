package tech.zuosi.deadbydaylight.util;

/**
 * Created by iwar on 2016/7/11.
 */

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;


/**
 * 0.��ȡ�汾
 * Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3]
 * ---------------------------------------------
 * org.bukkit.craftbukkit.VERSION.CraftItemStack
 * net.minecraft.server.VERSION.NBTTagCompound
 * ---------------------------------------------
 * ����NBT��ǩ�����岽��
 * 1.ItemStackת��ΪCraftItemStack��Bukkit��ʵ���ࣩ�ٵ���
 * ����ת��ΪNMS ItemStack��asNMSCopy()��
 * 2.ͨ��NMS ItemStack���Ի�ȡ��Ʒ��NBTTagCompound��getTag()��,
 * ���Ϊ�գ���ȡһ���µ�NBTTagCompound�����������ʵ������
 * 3.�����������͵���NBTTagCompound��setter����
 * �������������ͻᱻ��װΪNBT����������,����ΪNBTBase�ࣩ
 * 4.ͨ��������÷�������������
 * 5.����NMS ItemStack��NBTTagCompoundΪ֮ǰʹ�ù��Ķ���
 * 6.�������CraftItemStack��asCraftMirror��������NMS ItemStack����
 * תΪCraftItemStack����֮��ǿתΪItemStack���󣬷��ض���
 * ---------------------------------------------
 * ��ȡNBT��ǩ�����岽��
 * 1.ItemStack->NMS ItemStack
 * 2.ͨ��NMS ItemStack���Ի�ȡ��Ʒ��NBTTagCompound��getTag()��,
 * ���Ϊ�գ���ȡһ���µ�NBTTagCompound�����������ʵ������
 * 3.֮��ͨ���ö����getter����
 * ---------------------------------------------
 * �漰����
 * 1.Class forName() new Class[] {}
 * 2.Method invoke()
 * 3.Object
 */
public class NBTUtil {
    //TODO �����޷�Ӧ�÷���ȡ��NBTʱж�ز����Ӱ�����й�NBT���ж���
    //TODO NBTUtil�Ƿ��Ҫ��

    private final static String VERSION = Bukkit.getServer().getClass().getPackage().getName().replace(".",",").split(",")[3];

    public NBTUtil() {}

    private static Class getCraftItemStack() {
        try {
            return Class.forName("org.bukkit.craftbukkit." + VERSION + ".inventory.CraftItemStack");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("[����ɱ�����]NBTUtil�޷���������,���ܲ�֧�ֵ�ǰ�汾�ķ����.");
            cnfe.printStackTrace();
            return null;
        }
    }

    private static Object asNMSCopy(ItemStack itemStack) {
        Class cis = getCraftItemStack();
        try {
            return cis.getMethod("asNMSCopy",ItemStack.class).invoke(cis, itemStack);
        } catch (Exception e) {
            System.out.println("[����ɱ�����]NBTUtil�޷���������,���ܲ�֧�ֵ�ǰ�汾�ķ����.");
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
            System.out.println("[����ɱ�����]NBTUtil�޷���������,���ܲ�֧�ֵ�ǰ�汾�ķ����.");
            e.printStackTrace();
            return null;
        }
    }

    private static Object getNewTagCompound() {
        try {
            return Class.forName("net.minecraft.server." + VERSION + ".NBTTagCompound").newInstance();
        } catch (Exception e) {
            System.out.println("[����ɱ�����]NBTUtil�޷���������,���ܲ�֧�ֵ�ǰ�汾�ķ����.");
            e.printStackTrace();
            return null;
        }
    }

    private static Object getTagCompound(Object nmsitem) {
        try {
            return nmsitem.getClass().getMethod("getTag",new Class[0]).invoke(nmsitem);
        } catch (Exception e) {
            System.out.println("[����ɱ�����]NBTUtil�޷���������,���ܲ�֧�ֵ�ǰ�汾�ķ����.");
            e.printStackTrace();
            return null;
        }
    }

    private static Object setTagCompound(Object tagcompound,Object nmsitem) {
        try {
            nmsitem.getClass().getMethod("setTag",tagcompound.getClass()).invoke("setTag",tagcompound);
            return nmsitem;
        } catch (Exception e) {
            System.out.println("[����ɱ�����]NBTUtil�޷���������,���ܲ�֧�ֵ�ǰ�汾�ķ����.");
            e.printStackTrace();
            return null;
        }
    }

    public static ItemStack setInt(ItemStack is,String key,int value) {
        Object nmsitem = asNMSCopy(is);
        if (nmsitem == null) {
            System.out.println("[����ɱ�����]NBTUtil�޷���������,���ܲ�֧�ֵ�ǰ�汾�ķ����");
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
            System.out.println("[����ɱ�����]NBTUtil�޷���������,���ܲ�֧�ֵ�ǰ�汾�ķ����");
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
            System.out.println("[����ɱ�����]NBTUtil�޷���������,���ܲ�֧�ֵ�ǰ�汾�ķ����");
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
            System.out.println("[����ɱ�����]NBTUtil�޷���������,���ܲ�֧�ֵ�ǰ�汾�ķ����");
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
