package net.myplugin.visualintensify.menu;

import net.myplugin.visualintensify.VisualIntensify;
import net.myplugin.visualintensify.util.NBTUtil;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Wool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iwar on 2016/2/3.
 */
public class MenuManager implements SlotValue {
    private VisualIntensify plugin;

    
    public MenuManager(VisualIntensify plugin) {
        this.plugin = plugin;
    }

    public enum MenuType {
        MAIN,      //���˵�
        ITEM,      //��ȡ��Ʒ
        INTENSIFY, //ǿ��
        REBORN,    //ת��
        GEM,       //��Ƕ
        REMOVE;    //ժȡ

        MenuType() {
        }
    }

    public ItemStack viMenu (ItemStack is,String name) {
        NBTUtil.writeMenu(is,name);
        return is;
    }

    public ItemStack viInfo (String name) {
        ItemStack is = new ItemStack(Material.SIGN);
        ItemMeta im = is.getItemMeta();
        List<String> stringList = new ArrayList<>();
        im.setDisplayName(name);
        stringList.add(0,"vi:��Ϣ");
        im.setLore(stringList);
        is.setItemMeta(im);
        return is;
    }

    public ItemStack viItem (ItemStack is,String displayName,String typeName) {
        NBTUtil.writeItem(is,displayName,typeName);
        return is;
    }

    public ItemStack viItem (ItemStack is,String displayName,String typeName,String description) {
        String[] args = {description};
        NBTUtil.writeItem(is,displayName,typeName);
        NBTUtil.rewriteLore(is,args);
        return is;
    }

    public boolean isNothing(Inventory inventory,int INT) {
        return null == inventory.getItem(INT) || Material.AIR.equals(inventory.getItem(INT).getType());
    }

    public boolean canOperateType(ItemStack itemStack) {
        return plugin.getConfig().getBoolean("list."+itemStack.getType().name());
    }

    public Inventory createGUI(MenuType type) {
        Inventory editor;
        int i;
        if (type == MenuType.MAIN) {
            editor = Bukkit.createInventory(null, 54, "IntensifyPanel");
            for (i = 0; i < editor.getSize(); ++i) {
                editor.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
            }
            //editor.setItem(,viMenu(new ItemStack(Material),""));
            editor.setItem(0, viMenu(new ItemStack(Material.WORKBENCH), "��Ʒ�˵�"));
            editor.setItem(1, viMenu(new ItemStack(Material.ANVIL), "ǿ���˵�"));
            editor.setItem(2, viMenu(new ItemStack(Material.EMERALD), "��Ƕ�˵�"));
            editor.setItem(3, viMenu(new ItemStack(Material.EGG), "ת���˵�"));
            editor.setItem(4, viMenu(new ItemStack(Material.WOOD_AXE), "ժȡ�˵�"));
            editor.setItem(8, viMenu((new Wool(DyeColor.GREEN)).toItemStack(), "�ر�"));

            return editor;
        } else if (type == MenuType.ITEM) {
            editor = Bukkit.createInventory(null, 54, "IntensifyPanel");
            for (i = 0; i < editor.getSize(); ++i) {
                editor.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
            }
            //editor.setItem(,viItem(new ItemStack(Material),""));
            editor.setItem(0, viItem(new ItemStack(Material.EMERALD), "��dǿ��ʯ","ǿ��ʯ","��2ǿ��ʯ������ǿ���������"));
            editor.setItem(1, viItem(new ItemStack(Material.EMERALD), "��d����ϵǿ��ʯ","����ϵǿ��ʯ","��2����ǿ��ʯ��������ǿ���ɹ��ļ���"));
            editor.setItem(2, viItem(new ItemStack(Material.PAPER), "��d��ȫ����","��ȫ����","��2��ȫ�������������������ǿ��ʧ��Ҳ�ܱ�������"));
            editor.setItem(3, viItem(new ItemStack(Material.NETHER_STAR), "��cת��ʯ","ת��ʯ","��2ת��ʯ��ʹ�������ת�������Ʒ��"));
            editor.setItem(4, viItem(new ItemStack(Material.MAGMA_CREAM), "��e����","��������-����","��2������ģ�ʹ�������ӵ��������Ч"));
            editor.setItem(5, viItem(new ItemStack(Material.EYE_OF_ENDER), "��eĩӰ","��������-ĩӰ","��2ĩӰ���ģ�ʹ�������ӵ��������Ч"));
            editor.setItem(6, viItem(new ItemStack(Material.SHEARS), "��d�ɼ�����","�ɼ�����","��2�ɼ����ߣ�����ı�ʯ��ժ����"));
            editor.setItem(7, viItem(new ItemStack(Material.EMERALD), "�����˺�����","��ʯ-�����˺�����"));
            editor.setItem(8, viItem(new ItemStack(Material.EMERALD), "������������","��ʯ-������������"));
            editor.setItem(9, viItem(new ItemStack(Material.EMERALD), "��Ѫ","��ʯ-��Ѫ"));
            editor.setItem(10, viItem(new ItemStack(Material.EMERALD), "����","��ʯ-����"));
            editor.setItem(11, viItem(new ItemStack(Material.EMERALD), "����ӳ�","��ʯ-����ӳ�"));
            editor.setItem(12, viItem(new ItemStack(Material.EMERALD), "�����ƶ�","��ʯ-�����ƶ�"));
            editor.setItem(16, viInfo("�����м�����һ��"));
            editor.setItem(17, viMenu((new Wool(DyeColor.GREEN)).toItemStack(), "�ر�"));

            return editor;
        } else if (type == MenuType.INTENSIFY) {
            editor = Bukkit.createInventory(null, 54, "IntensifyPanel");
            for (i = 0; i < editor.getSize(); ++i) {
                editor.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
            }
            editor.setItem(MATERIAL-1, viInfo("�Ҳ�����ǿ����Ʒ"));
            editor.setItem(MATERIAL, new ItemStack(Material.AIR));
            editor.setItem(UP-1, viInfo("�Ҳ����ǿ��ʯ������ǿ��ʯ"));
            editor.setItem(UP, new ItemStack(Material.AIR));
            editor.setItem(PRODUCT+1, viInfo("�˴�������ǿ������Ʒ"));
            editor.setItem(PRODUCT, new ItemStack(Material.AIR));
            editor.setItem(DOWN-1, viInfo("�Ҳ���밲ȫ����"));
            editor.setItem(DOWN, new ItemStack(Material.AIR));
            editor.setItem(25, viMenu(new ItemStack(Material.ANVIL), "ǿ��"));
            editor.setItem(26, viMenu((new Wool(DyeColor.GREEN)).toItemStack(), "����"));

            return editor;
        } else if (type == MenuType.REBORN) {
            editor = Bukkit.createInventory(null, 54, "IntensifyPanel");
            for (i = 0; i < editor.getSize(); ++i) {
                editor.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
            }
            editor.setItem(MATERIAL-1, viInfo("�Ҳ�����ת����Ʒ"));
            editor.setItem(MATERIAL, new ItemStack(Material.AIR));
            editor.setItem(PRODUCT+1, viInfo("�˴�������ת������Ʒ"));
            editor.setItem(PRODUCT, new ItemStack(Material.AIR));
            editor.setItem(DOWN-1, viInfo("�Ҳ����ת��ʯ"));
            editor.setItem(DOWN, new ItemStack(Material.AIR));
            editor.setItem(UP-1, viInfo("�Ҳ������������"));
            editor.setItem(UP, new ItemStack(Material.AIR));
            editor.setItem(25, viMenu(new ItemStack(Material.EGG), "ת��"));
            editor.setItem(26, viMenu((new Wool(DyeColor.GREEN)).toItemStack(), "����"));

            return editor;
        } else if(type == MenuType.REMOVE) {
            editor = Bukkit.createInventory(null, 54, "IntensifyPanel");
            for (i = 0; i < editor.getSize(); ++i) {
                editor.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
            }
            editor.setItem(MATERIAL-1, viInfo("�Ҳ�����������Ʒ"));
            editor.setItem(MATERIAL, new ItemStack(Material.AIR));
            editor.setItem(PRODUCT+1, viInfo("�˴������ɴ������Ʒ"));
            editor.setItem(PRODUCT, new ItemStack(Material.AIR));
            editor.setItem(PRODUCT_GEM+1, viInfo("�˴������ɴ����ʯ"));
            editor.setItem(PRODUCT_GEM, new ItemStack(Material.AIR));
            editor.setItem(DOWN-1, viInfo("�Ҳ����ɼ�����"));
            editor.setItem(DOWN, new ItemStack(Material.AIR));
            editor.setItem(25, viMenu(new ItemStack(Material.WOOD_AXE), "ժȡ"));
            editor.setItem(26, viMenu((new Wool(DyeColor.GREEN)).toItemStack(), "����"));

            return editor;
        } else if(type != MenuType.GEM) {
            return null;
        } else {
            editor = Bukkit.createInventory(null, 54, "IntensifyPanel");
            for(i = 0; i < editor.getSize(); ++i) {
                editor.setItem(i, new ItemStack(Material.STAINED_GLASS_PANE));
            }
            editor.setItem(UP-1, viInfo("�Ҳ���뱦ʯ"));
            editor.setItem(UP, new ItemStack(Material.AIR));
            editor.setItem(MATERIAL-1, viInfo("�Ҳ�����������Ʒ"));
            editor.setItem(MATERIAL, new ItemStack(Material.AIR));
            editor.setItem(PRODUCT+1, viInfo("�˴������ɴ������Ʒ"));
            editor.setItem(PRODUCT, new ItemStack(Material.AIR));
            editor.setItem(16,viMenu(new ItemStack(Material.EMERALD),"��Ƕ"));
            editor.setItem(17,viMenu((new Wool(DyeColor.GREEN)).toItemStack(),"����"));

            return editor;
        }
    }

}
