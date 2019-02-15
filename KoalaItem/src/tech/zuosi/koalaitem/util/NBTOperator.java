package tech.zuosi.koalaitem.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.item.ItemGem;
import tech.zuosi.koalaitem.type.CoreType;
import tech.zuosi.koalaitem.type.GemType;
import tech.zuosi.koalaitem.type.ItemQuality;
import tech.zuosi.koalaitem.type.ItemType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by iwar on 2016/7/18.
 */
public class NBTOperator {
    private NBTUtil util;

    public NBTOperator(NBTUtil util) {
        this.util = util;
    }

    public boolean identify() {
        if (util.getItemData("quality") != null) return false;
        util.initData("bala", ItemType.PLAYERITEM,new String[]{"0","0","0"});
        return true;
    }

    //PLAYERITEM
    public boolean intensify() {
        int level = (Integer) util.getItemData("intensifyLevel");
        level++;
        util.setItemData("intensifyLevel",level);

        return true;
    }

    public boolean intensify(int chance,boolean isSafe) {
        Random random = new Random();
        int level = (Integer) util.getItemData("intensifyLevel");
        if (chance <= random.nextInt(100)) {
            if (!isSafe) {
                level--;
                util.setItemData("intensifyLevel",level);
            }
            return false;
        } else {
            level++;
            util.setItemData("intensifyLevel",level);
            return true;
        }
    }

    public boolean drill() {
        String key = "";
        if (GemType.VOID == GemType.valueOf((String)util.getItemData("gemOne"))) {
            key+="gemOne";
        } else if (GemType.VOID == GemType.valueOf((String)util.getItemData("gemTwo"))) {
            key+="gemTwo";
        } else if (GemType.VOID == GemType.valueOf((String)util.getItemData("gemThree"))) {
            key+="gemThree";
        } else {
            return false;
        }

        return util.setItemData(key,GemType.EMPTY.name());
    }

    public boolean inlay(ItemGem itemGem) {
        GemType gemType = itemGem.getGemType();
        int gemLevel = itemGem.getLevel();
        String key = "";
        GemType one = GemType.valueOf((String)util.getItemData("gemOne"));
        GemType two = GemType.valueOf((String)util.getItemData("gemTwo"));
        GemType three = GemType.valueOf((String)util.getItemData("gemThree"));
        if (GemType.EMPTY == one) {
            key+="gemOne";
        } else if (GemType.EMPTY == two) {
            if (two.equals(one)) return false;
            key+="gemTwo";
        } else if (GemType.EMPTY == three) {
            if (three.equals(one)) return false;
            if (three.equals(two)) return false;
            key+="gemThree";
        } else {
            return false;
        }

        return util.setItemData(key,gemType.name()) && util.setItemData(key + "Level",gemLevel);
    }

    public boolean reborn(CoreType coreType) {
        if ((int)util.getItemData("rebornLevel") == 6) return false;
        util.setItemData("intensifyLevel", 0);
        util.setItemData("rebornLevel", (int)util.getItemData("rebornLevel")+1);
        util.setItemData("quality", ItemQuality.NORMAL.getTypeName((int)util.getItemData("rebornLevel")));
        if (CoreType.EMPTY != CoreType.valueOf((String)util.getItemData("coreType"))) return false;
        util.setItemData("coreType", coreType.name());

        if ((int)util.getItemData("rebornLevel") >= 2)
            pointAttribute();
        if ((int)util.getItemData("rebornLevel") >= 4)
            pointSkill();

        return true;
    }

    public void reborn() {
        if ((int)util.getItemData("rebornLevel") == 6) return;
        util.setItemData("intensifyLevel", 0);
        util.setItemData("rebornLevel", (int)util.getItemData("rebornLevel")+1);
        util.setItemData("quality", ItemQuality.NORMAL.getTypeName((int)util.getItemData("rebornLevel")));

        if ((int)util.getItemData("rebornLevel") >= 2)
            pointAttribute();
        if ((int)util.getItemData("rebornLevel") >= 4)
            pointSkill();
    }

    public int remove(int count) {
        if (util.isGemExist(GemType.valueOf((String)util.getItemData("gemOne")))) {
            if (count >= 1) {
                //获取对应物品
                util.setItemData("gemOne",GemType.EMPTY.name());
            }
            count--;
        }
        if (util.isGemExist(GemType.valueOf((String)util.getItemData("gemTwo")))) {
            if (count >= 1) {
                //获取对应物品
                util.setItemData("gemTwo",GemType.EMPTY.name());
            }
            count--;
        }
        if (util.isGemExist(GemType.valueOf((String)util.getItemData("gemThree")))) {
            if (count >= 1) {
                //获取对应物品
                util.setItemData("gemThree",GemType.EMPTY.name());
            }
            count--;
        }

        return count;
    }

    //WINE
    public NBTUtil extract() {
        ItemStack potion = new ItemStack(Material.POTION);
        String isName = util.getItemStack().getType().name();
        potion.setDurability((short)0);
        util.setItemStack(potion);
        util.initData(isName,ItemType.RESOLVENT,new String[]{"0","0","0"})
            .initLore(new String[]{ChatColor.GOLD + "提炼物-" + isName})
            .setDisplayName("提炼产物");

        return util;
    }

    public NBTUtil brew(String[] wineString) {
        util.initData("bala",ItemType.WINE,wineString)
            .initLore(new String[]{
                    ChatColor.GREEN + "【功能】" + ChatColor.LIGHT_PURPLE + (String)util.getWineData("effect"),
                    ChatColor.GREEN + "【用法】" + ChatColor.RED + "右键饮用"
                    /*ChatColor.GOLD + (String)util.getWineData("id"),
                    ChatColor.GREEN + (String)util.getWineData("recipe"),
                    ChatColor.GREEN + (String)util.getWineData("effect")*/
            }).setDisplayName(ChatColor.GREEN + "药水");

        return util;
    }

    private void pointAttribute() {
        int AGL = (int)util.getAttribute("AGL");
        int ATK = (int)util.getAttribute("ATK");
        int MGA = (int)util.getAttribute("MGA");
        int DEX = (int)util.getAttribute("DEX");
        int Max = KoalaItem.INSTANCE.getConfig().getInt("Attribute.Max");

        int randomValue = new Random().nextInt(Max);

        if (AGL == 0) {
            util.setAttribute("AGL",randomValue);
        } else if (ATK == 0) {
            util.setAttribute("ATK",randomValue);
        } else if (MGA == 0) {
            util.setAttribute("MGA",randomValue);
        } else if (DEX == 0) {
            util.setAttribute("DEX",randomValue);
        }
    }

    private void pointSkill() {
        String skillOne = util.getSkill("skillOne");
        String skillTwo = util.getSkill("skillTwo");
        Iterator iterator = KoalaItem.INSTANCE.getConfig().getConfigurationSection("SkillList").getKeys(false).iterator();
        List<String> skillList = new ArrayList<>();

        while (iterator.hasNext()) {
            skillList.add((String) iterator.next());
        }

        int SIZE = skillList.size();
        int index = new Random().nextInt(SIZE);


        if (skillOne.equals("未激活")) {
            util.setSkill("skillOne",skillList.get(index));
        } else if (skillTwo.equals("未激活")) {
            if (util.getSkill("skillOne").equals(skillList.get(index))) {
                skillList.remove(skillList.get(index));
                index = new Random().nextInt(skillList.size());
            }

            util.setSkill("skillTwo",skillList.get(index));
        }
    }
}
