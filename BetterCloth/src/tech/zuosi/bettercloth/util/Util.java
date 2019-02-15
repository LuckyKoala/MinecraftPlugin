package tech.zuosi.bettercloth.util;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import tech.zuosi.bettercloth.BetterCloth;

import java.util.List;

public class Util {
    private double tmp;
	private static BetterCloth sr;
	public final static int DISPLAYNAME = 5;
    public final static int MAXPOINT = 4;
    public final static int POINT = 3;
    public final static int RESTORESPEED = 2;
    public final static int SHEILD = 1;
    //public static double SHEILDVALUE;

	public Util(BetterCloth args){
		sr=args;
        //tmp = sr.SHEILD;
        //SHEILDVALUE = tmp;
	}
	public static void giveItem(Player p,String name){
		ItemStack item = Util.PointItem(name);
		p.getInventory().addItem(item);
	}
	public static ItemStack PointItem(String name){
		ItemStack item = Item.getItem(name);
		ItemMeta meta = item.getItemMeta();
		List<String> a = meta.getLore();
        String t = sr.getConfig().getString("item."+name+".DisplayName");
        String s = t.replaceAll("&","§");
        a.add(s);
		a.add("§a最大防御点数:§e"+sr.getConfig().getInt("item."+name+".MAXPoint"));
		a.add("§a防御点数:§e"+sr.getConfig().getInt("item."+name+".MAXPoint"));
        a.add("§a恢复时间:§e"+sr.getConfig().getInt("item."+name+".RestoreSpeed"));
        a.add("§a伤害单位:§e"+sr.getConfig().getDouble("item."+name+".Sheild"));
		meta.setLore(a);
		item.setItemMeta(meta);
		return item;
	}
	public static double deDamage(Player p,double primaryDamage) {
        double finalDamage = primaryDamage;
        boolean hasDeDamage = false;

        ItemStack[] armor = p.getInventory().getArmorContents();
        for (ItemStack is:armor) {
            if (is==null || is.getType() == Material.AIR) continue;
            if (!is.hasItemMeta() || !is.getItemMeta().hasLore()) continue;
            if (!is.getItemMeta().hasDisplayName()) continue;

            if (finalDamage != 0.0) {
                ItemMeta meta = is.getItemMeta();
                List<String> lore = meta.getLore();
                String displayName = meta.getDisplayName();
                String TYPENAME = is.getType().name();
                int SIZE = lore.size();
                if (SIZE>=5 && lore.get(SIZE-DISPLAYNAME).equalsIgnoreCase(displayName)) {
                    int prePoint = getInt(lore.get(SIZE-POINT));
                    double sheildValue = getDouble(lore.get(SIZE-SHEILD));
                    if (prePoint > 0) {
                        if (TYPENAME.contains("HELMET")) {
                            Data.helmetMap.put(p, getInt(lore.get(SIZE-RESTORESPEED)));
                        } else if (TYPENAME.contains("CHESTPLATE")) {
                            Data.chestplateMap.put(p, getInt(lore.get(SIZE-RESTORESPEED)));
                        } else if (TYPENAME.contains("LEGGINGS")) {
                            Data.leggingsMap.put(p, getInt(lore.get(SIZE-RESTORESPEED)));
                        } else if (TYPENAME.contains("BOOTS")) {
                            Data.bootsMap.put(p, getInt(lore.get(SIZE-RESTORESPEED)));
                        }

                        int minusNumber;
                        double damageUnit = finalDamage/sheildValue;
                        double maxAbsorbUnit = prePoint; // 1
                        boolean canTotallyAbsorb = (damageUnit - maxAbsorbUnit) <= 0;
                        System.out.println("---------");
                        System.out.println("finalDamage:" + finalDamage + "||sheildValue:" + sheildValue
                        + "||damageUnit:" + damageUnit);
                        System.out.println("prePoint:" + prePoint + "||maxAbsorbUnit:" + maxAbsorbUnit);
                        System.out.println("canTotallyAbsorb:" + canTotallyAbsorb);
                        if (canTotallyAbsorb) {
                            finalDamage = 0.0;
                            if ((damageUnit - (int)damageUnit) > 0) {
                                minusNumber = (int)damageUnit + 1;
                            } else {
                                minusNumber = (int)damageUnit;
                            }
                            if (minusNumber/sheildValue - (minusNumber/sheildValue) != 0) {
                                minusNumber = (int)(minusNumber/sheildValue) + 1;
                            }
                        } else {
                            finalDamage = (damageUnit-maxAbsorbUnit)*sheildValue;
                            minusNumber = prePoint;
                        }

                        prePoint-=minusNumber;
                        if (prePoint < 0) prePoint=0;
                        lore.set(SIZE-POINT,"§a防御点数:§e"+prePoint);
                        meta.setLore(lore);
                        is.setItemMeta(meta);

                        if (!hasDeDamage) hasDeDamage=true;

                        if (!Data.lockArmor.contains(p)) {
                            Data.lockArmor.add(p);
                            ActionBar.sendAction(p, ChatColor.RED + "遭受攻击，锁定装备栏.");
                            Bukkit.getScheduler().scheduleSyncDelayedTask(sr, () -> {
                                if (Data.lockArmor.contains(p)) {
                                    Data.lockArmor.remove(p);
                                    ActionBar.sendAction(p, ChatColor.GOLD + "您的装备栏已经解锁.");
                                }
                            }, 200L);
                        }
                    }
                }
            } else {
                return 0.0;
            }

        }
        Data.whetherToShowMessage.put(p,hasDeDamage);

        return finalDamage;
	}

    public static int getInt(String str){
        String temp = "";
        if(!str.isEmpty() && !"".equals(str)){
            for(int i=2;i<str.length();i++){
                if((str.charAt(i)>=48 && str.charAt(i)<=57)){
                    temp+=str.charAt(i);
                }
            }
        }
        return Integer.parseInt(temp);
    }

    public static double getDouble(String str) {
        //char 46 . 不加判断则会影响非整数的获得
        String temp = "";
        if(!str.isEmpty() && !"".equals(str)){
            for(int i=2;i<str.length();i++){
                if((str.charAt(i)>=48 && str.charAt(i)<=57) || str.charAt(i) == 46){
                    temp+=str.charAt(i);
                }
            }
        }
        return Double.parseDouble(temp);
    }
}
