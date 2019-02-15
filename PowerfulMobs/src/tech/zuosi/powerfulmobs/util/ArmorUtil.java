package tech.zuosi.powerfulmobs.util;

import org.bukkit.inventory.PlayerInventory;

/**
 * Created by iwar on 2016/5/11.
 */
public class ArmorUtil {
    public static double getDamageReducePercent(PlayerInventory playerInventory) {
        return getTotalArmorValue(playerInventory)*0.04;
    }
    public static int getTotalArmorValue(PlayerInventory pi) {
        int total = 0;
        if (pi.getHelmet() != null) {
            switch (pi.getHelmet().getType()) {
                case LEATHER_HELMET:
                    total += 1;
                    break;
                case CHAINMAIL_HELMET:
                    total += 2;
                    break;
                case IRON_HELMET:
                    total += 2;
                    break;
                case GOLD_HELMET:
                    total += 2;
                    break;
                case DIAMOND_HELMET:
                    total += 3;
                    break;
                default:
                    break;
            }
        }
        if (pi.getChestplate() != null) {
            switch (pi.getChestplate().getType()) {
                case LEATHER_CHESTPLATE:
                    total += 3;
                    break;
                case CHAINMAIL_CHESTPLATE:
                    total += 5;
                    break;
                case IRON_CHESTPLATE:
                    total += 6;
                    break;
                case GOLD_CHESTPLATE:
                    total += 5;
                    break;
                case DIAMOND_CHESTPLATE:
                    total += 8;
                    break;
                default:
                    break;
            }
        }
        if (pi.getLeggings() != null) {
            switch (pi.getLeggings().getType()) {
                case LEATHER_LEGGINGS:
                    total += 2;
                    break;
                case CHAINMAIL_LEGGINGS:
                    total += 4;
                    break;
                case IRON_LEGGINGS:
                    total += 5;
                    break;
                case GOLD_LEGGINGS:
                    total += 3;
                    break;
                case DIAMOND_LEGGINGS:
                    total += 6;
                    break;
                default:
                    break;
            }
        }
        if (pi.getBoots() != null) {
            switch (pi.getBoots().getType()) {
                case LEATHER_BOOTS:
                    total += 1;
                    break;
                case CHAINMAIL_BOOTS:
                    total += 1;
                    break;
                case IRON_BOOTS:
                    total += 2;
                    break;
                case GOLD_BOOTS:
                    total += 1;
                    break;
                case DIAMOND_BOOTS:
                    total += 3;
                    break;
                default:
                    break;
            }
        }
        return total;
    }
}
