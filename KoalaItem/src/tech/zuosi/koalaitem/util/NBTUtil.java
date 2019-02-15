package tech.zuosi.koalaitem.util;

import me.dpohvar.powernbt.PowerNBT;
import me.dpohvar.powernbt.api.NBTCompound;
import me.dpohvar.powernbt.api.NBTList;
import me.dpohvar.powernbt.api.NBTManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tech.zuosi.koalaitem.type.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by iwar on 2016/7/16.
 */
public class NBTUtil {
    static {
        manager = PowerNBT.getApi();
    }

    private static NBTManager manager;
    private NBTCompound compound;
    private ItemStack itemStack = new ItemStack(Material.STAINED_GLASS_PANE);

    public NBTUtil(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.compound = manager.read(itemStack);
        if (compound == null) this.compound = new NBTCompound();
    }

    //DisplayUtil
    private NBTCompound getDisplayCompound() {
        return compound.getCompound("display")==null?new NBTCompound():compound.getCompound("display");
    }

    public NBTList getDisplayLore() {
        NBTCompound display = getDisplayCompound();
        return display.getList("Lore")==null?new NBTList():display.getList("Lore");
    }

    private void rewriteLore(String[] args) {
        NBTList lore = new NBTList(args);
        NBTCompound display = getDisplayCompound();
        display.put("Lore",lore);
        compound.put("display",display);
        manager.write(itemStack,compound);
    }

    public NBTUtil setDisplayName(String displayName) {
        NBTCompound display = getDisplayCompound();
        display.put("Name",displayName);
        compound.put("display",display);
        manager.write(itemStack,compound);

        return this;
    }

    //Display
    public NBTUtil initLore(String[] description) {
        rewriteLore(description);
        return this;
    }

    public NBTUtil updateDisplayData() {
        //Only ItemType.PLAYERITEM
        if (!compound.containsKey("viData")) return null;
        NBTCompound viData = compound.compound("viData");
        if (ItemType.PLAYERITEM != ItemType.valueOf(viData.getString("type"))) return null;
        FormatUtil formatUtil = new FormatUtil();
        String[] data = {
                ChatColor.GOLD + "----------------------------",
                ChatColor.DARK_GREEN + "品质:" + ChatColor.RED + " " + ItemQuality.valueOf((String)getItemData("quality")).getName(),
                ChatColor.DARK_GREEN + "强化:" + ChatColor.RED + " " + getItemData("intensifyLevel") + "级",
                ChatColor.DARK_GREEN + "转生:" + ChatColor.RED + " " + getItemData("rebornLevel") + "级",
                ChatColor.DARK_GREEN + "宝石:" + ChatColor.RED + " "
                        + GemType.valueOf((String)getItemData("gemOne")).getName() + "["  + getItemData("gemOneLevel") + "],"
                        + GemType.valueOf((String)getItemData("gemTwo")).getName() + "["  + getItemData("gemTwoLevel") + "],"
                        + GemType.valueOf((String)getItemData("gemThree")).getName() + "["  + getItemData("gemThreeLevel") + "]",
                ChatColor.DARK_GREEN + "核心:" + ChatColor.RED + " " + CoreType.valueOf((String)getItemData("coreType")).getName(),
                ChatColor.GOLD + "----------------------------",
                ChatColor.GREEN + "属性:",
                "   " + ChatColor.AQUA + "敏捷:" + " " + formatUtil.starLevel((int)getAttribute("AGL")),
                "   " + ChatColor.AQUA + "会心:" + " " + formatUtil.starLevel((int)getAttribute("DEX")),
                "   " + ChatColor.AQUA + "力量:" + " " + formatUtil.starLevel((int)getAttribute("ATK")),
                "   " + ChatColor.AQUA + "灵力:" + " " + formatUtil.starLevel((int)getAttribute("MGA")),
                ChatColor.LIGHT_PURPLE + "技能:",
                "   " + ChatColor.DARK_PURPLE + "1号位:" + ChatColor.RED + " " + getSkill("skillOne"),
                "   " + ChatColor.DARK_PURPLE + "2号位:" + ChatColor.RED + " " + getSkill("skillTwo"),
                ChatColor.GOLD + "----------------------------",
                ChatColor.GRAY + "ID:" + " " + ChatColor.RED + getItemData("timeStamp")
        };
        rewriteLore(data);

        return this;
    }

    //Data
    public NBTUtil initData(String name,ItemType itemType,String[] wineString) {
        NBTCompound viData = new NBTCompound();
        viData.put("isInit",true);
        viData.put("type",itemType.name());
        if (ItemType.PLAYERITEM == itemType) {
            NBTCompound viItemData = new NBTCompound();
            viItemData.put("quality", ItemQuality.NORMAL.name());
            viItemData.put("intensifyLevel", 0);
            viItemData.put("rebornLevel", 0);
            viItemData.put("gemOne", GemType.VOID.name());
            viItemData.put("gemOneLevel", 0);
            viItemData.put("gemTwo", GemType.VOID.name());
            viItemData.put("gemTwoLevel", 0);
            viItemData.put("gemThree", GemType.VOID.name());
            viItemData.put("gemThreeLevel", 0);
            viItemData.put("coreType", CoreType.EMPTY.name());
            viItemData.put("timeStamp", System.currentTimeMillis());

            NBTCompound attributes = new NBTCompound();
            //AGL 敏捷 DEX 会心 ATK 力量 MGA 灵力
            attributes.put("AGL", 0);
            attributes.put("DEX", 0);
            attributes.put("ATK", 0);
            attributes.put("MGA", 0);

            NBTCompound skills = new NBTCompound();
            skills.put("skillOne", "未激活");
            skills.put("skillTwo", "未激活");

            viItemData.put("attributes",attributes);
            viItemData.put("skills",skills);
            viData.put("viItemData", viItemData);
        } else if (ItemType.WINE == itemType) {
            NBTCompound viWineData = new NBTCompound();
            viWineData.put("id", wineString[0]);
            viWineData.put("recipe", wineString[1]);
            viWineData.put("effect", wineString[2]);
            viData.put("viWineData", viWineData);
        } else {
            viData.put("name",name);
            if (ItemType.MENU == itemType) {
                NBTCompound viMenuData = new NBTCompound();
                viMenuData.put("isReturn", 0);
                viMenuData.put("menuType", MenuType.UNKNOWN.name());
            } else if (ItemType.GEM == itemType) {
                viData.put("gemLevel", 0);
            }
        }
        compound.put("viData",viData);
        manager.write(itemStack,compound);

        return this;
    }

    public Object getWineData(String key) {
        if (!compound.containsKey("viData") || !compound.compound("viData").containsKey("viWineData")) {
            return null;
        }
        NBTCompound viWineData = compound.compound("viData").compound("viWineData");

        return viWineData.get(key);
    }

    public List<PotionEffect> getWineEffect() {
        if (!compound.containsKey("viData") || !compound.compound("viData").containsKey("viWineData")) {
            return null;
        }
        //TODO 异常处理
        NBTCompound viWineData = compound.compound("viData").compound("viWineData");
        String[] effectString = ((String) viWineData.get("effect")).split("/");
        List<PotionEffect> potionEffectList = new ArrayList<>();
        for (String e : effectString) {
            String[] E = e.split("-");
            int level = Integer.parseInt(E[2])-1;
            level = level>=0?level:0;
            potionEffectList.add(new PotionEffect(PotionEffectType.getByName(E[0]),Integer.parseInt(E[1]),
                    level));
        }
        return potionEffectList;
    }

    public Object getData(String key) {
        if (!compound.containsKey("viData")) {
            return null;
        }
        NBTCompound viData = compound.compound("viData");
        return viData.get(key);
    }

    public boolean setData(String key,Object value) {
        if (!compound.containsKey("viData")) {
            return false;
        }
        NBTCompound viData = compound.compound("viData");
        if (!viData.containsKey(key)) return false;
        viData.put(key,value);
        compound.put("viData",viData);
        manager.write(itemStack,compound);

        return true;
    }

    public Object getItemData(String key) {
        if (!compound.containsKey("viData") || !compound.compound("viData").containsKey("viItemData")) {
            return null;
        }
        NBTCompound viItemData = compound.compound("viData").compound("viItemData");

        return viItemData.get(key);
    }

    public boolean setItemData(String key,Object value) {
        if (!compound.containsKey("viData") || !compound.compound("viData").containsKey("viItemData")) {
            return false;
        }
        NBTCompound viData = compound.compound("viData");
        NBTCompound viItemData = viData.compound("viItemData");
        if (!viItemData.containsKey(key)) return false;
        viItemData.put(key,value);
        viData.put("viItemData",viItemData);
        compound.put("viData",viData);
        manager.write(itemStack,compound);

        return true;
    }

    public Object getAttribute(String key) {
        if (!compound.containsKey("viData") || !compound.compound("viData").containsKey("viItemData")
                || !compound.compound("viData").compound("viItemData").containsKey("attributes")) {
            return null;
        }
        NBTCompound attributes = compound.compound("viData").compound("viItemData")
                .compound("attributes");

        return attributes.get(key);
    }

    public boolean setAttribute(String key,Object value) {
        if (!compound.containsKey("viData") || !compound.compound("viData").containsKey("viItemData")
                || !compound.compound("viData").compound("viItemData").containsKey("attributes")) {
            return false;
        }
        NBTCompound viData = compound.compound("viData");
        NBTCompound viItemData = viData.compound("viItemData");
        NBTCompound attributes = compound.compound("viData").compound("viItemData")
                .compound("attributes");
        if (!attributes.containsKey(key)) return false;
        attributes.put(key, value);
        viItemData.put("attributes",attributes);
        viData.put("viItemData",viItemData);
        compound.put("viData",viData);
        manager.write(itemStack,compound);

        return true;
    }

    public String getSkill(String key) {
        if (!compound.containsKey("viData") || !compound.compound("viData").containsKey("viItemData")
                || !compound.compound("viData").compound("viItemData").containsKey("skills")) {
            return null;
        }
        NBTCompound skills = compound.compound("viData").compound("viItemData")
                .compound("skills");
        String skillName = (String) skills.get(key);
        if ("empty".equalsIgnoreCase(skillName)) return "无";

        return skillName;
    }

    public boolean setSkill(String key,Object value) {
        if (!compound.containsKey("viData") || !compound.compound("viData").containsKey("viItemData")
                || !compound.compound("viData").compound("viItemData").containsKey("skills")) {
            return false;
        }
        NBTCompound viData = compound.compound("viData");
        NBTCompound viItemData = viData.compound("viItemData");
        NBTCompound skills = compound.compound("viData").compound("viItemData")
                .compound("skills");
        if (!skills.containsKey(key)) return false;
        skills.put(key, value);
        viItemData.put("skills",skills);
        viData.put("viItemData",viItemData);
        compound.put("viData",viData);
        manager.write(itemStack,compound);

        return true;
    }

    boolean isGemExist(GemType gemType) {
        return GemType.VOID != gemType && GemType.EMPTY != gemType;
    }

    //Item
    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack newItemStack) {
        this.itemStack = newItemStack;
    }
}
