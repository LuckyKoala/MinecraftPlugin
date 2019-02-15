package tech.zuosi.koalaitem.handler.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.koalaitem.KoalaItem;
import tech.zuosi.koalaitem.gui.MakerUI;
import tech.zuosi.koalaitem.item.*;
import tech.zuosi.koalaitem.type.CoreType;
import tech.zuosi.koalaitem.type.ItemType;
import tech.zuosi.koalaitem.util.NBTOperator;
import tech.zuosi.koalaitem.util.NBTUtil;
import tech.zuosi.koalaitem.util.Validate;

/**
 * Created by iwar on 2016/7/19.
 */
public class Maker extends GuiHandler implements Listener {
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Inventory inv = e.getView().getTopInventory();
        if ("锻造台".equals(inv.getTitle())) {
            Player p = (Player) e.getWhoClicked();
            Material type = safeMaterial(e.getCurrentItem());
            int slot = e.getRawSlot();
            if (!isInCorrectInventory(inv,slot)) return;
            if (Material.AIR != type) {
                if (slot == 0 && Material.NETHER_STAR == type) {
                    ItemStack putIS = e.getCursor();
                    if (putIS == null || Material.AIR == putIS.getType()) {
                        if (new ItemReborn().validate(e.getCurrentItem())) {
                            inv.setItem(1,new NBTUtil(new ItemStack(Material.STAINED_GLASS_PANE))
                                    .initLore(new String[]{
                                    ChatColor.GOLD + "本锻造台支持以下功能",
                                    ChatColor.GREEN + "鉴定，强化，镶嵌，转生，打孔，熔炼",
                                    ChatColor.RED + "转生请先在工具处放入转生石，之后可选择在第二格放入核心"
                            }).initData("PANE", ItemType.INFO,new String[]{"0","0","0"})
                                    .setDisplayName(ChatColor.YELLOW + "锻造面板").getItemStack());
                            p.sendMessage(ChatColor.GOLD + "检测到转生石移出，已恢复常规锻造模式");
                        }
                    }
                    return;
                }
                if (Material.STAINED_GLASS_PANE == type || Material.SNOW_BALL == type
                        || Material.SLIME_BALL == type) {
                    if (!isInfo(e.getCurrentItem())) return;
                    e.setCancelled(true);
                    return;
                }
                if (Material.STONE_PLATE == type) {
                    if (!isMenu(e.getCurrentItem())) return;
                    e.setCancelled(true);
                    ItemStack tool,item,core;
                    tool = inv.getItem(0);
                    core = inv.getItem(1);
                    item = inv.getItem(2);
                    MakerUI makerUI = new MakerUI();
                    if (Material.AIR == safeMaterial(tool) || Material.AIR == safeMaterial(item))
                        return;
                    NBTUtil util = new NBTUtil(item);
                    NBTOperator opertor = new NBTOperator(util);
                    if (!isTool(tool)) {
                        p.sendMessage(ChatColor.RED + "非法的工具.");
                    }
                    if (Validate.isOperateLegal(util)) {
                        //打孔
                        if (new ItemDrill().validate(tool)) {
                            if (opertor.drill()) {
                                makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                            } else {
                                p.sendMessage(ChatColor.RED + "打孔失败,请检查武器属性");
                            }
                        } else if (new ItemGem().validate(tool)) {
                            //镶嵌
                            if (opertor.inlay(new ItemGem(tool))) {
                                makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                            } else {
                                p.sendMessage(ChatColor.RED + "镶嵌失败,请确保武器有空的镶嵌孔且镶嵌孔中没有同类宝石存在");
                            }
                        } else {
                            if (Material.STAINED_GLASS_PANE != safeMaterial(core)) {
                                if (Material.AIR == safeMaterial(core)) {
                                    int level = ((int)util.getItemData("intensifyLevel"));
                                    if (level != 10) {
                                        p.sendMessage(ChatColor.RED + "转生失败,武器强化等级未达到十级，请继续强化");
                                        return;
                                    }
                                    opertor.reborn();
                                    makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                } else if (new ItemCore().validate(core)) {
                                    //转生
                                    int level = ((int)util.getItemData("intensifyLevel"));
                                    if (level != 10) {
                                        p.sendMessage(ChatColor.RED + "转生失败,武器强化等级未达到十级，请继续强化");
                                        return;
                                    }
                                    CoreType coreType = new ItemCore(core).getCoreType();
                                    if (opertor.reborn(coreType)) {
                                        makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                    } else {
                                        p.sendMessage(ChatColor.RED + "转生失败，请检查武器是否已有核心");
                                    }
                                }
                            } else {
                                //强化
                                int level = ((int)util.getItemData("intensifyLevel"));
                                if (level > 5 && level <= 10) {
                                    if (level == 10) {
                                        p.sendMessage(ChatColor.RED + "强化失败,武器强化等级无法再提高，请尝试转生");
                                        return;
                                    }
                                    if (new ItemIntensify().validate(tool)) {
                                        if (opertor.intensify(KoalaItem.INSTANCE.getConfig().getInt("Chance.intensify"),false)) {
                                            makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                        } else {
                                            makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                            p.sendMessage(ChatColor.RED + "强化失败,武器降级");
                                        }
                                    } else if (new ItemLuckyIntensify().validate(tool)) {
                                        if (opertor.intensify(KoalaItem.INSTANCE.getConfig().getInt("Chance.lucky"),false)) {
                                            makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                        } else {
                                            makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                            p.sendMessage(ChatColor.RED + "强化失败,武器降级");
                                        }
                                    } else if (new ItemSafetyIntensify().validate(tool)) {
                                        if (opertor.intensify(KoalaItem.INSTANCE.getConfig().getInt("Chance.intensify"),true)) {
                                            makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                        } else {
                                            makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                            p.sendMessage(ChatColor.RED + "强化失败,武器保持原样");
                                        }
                                    }
                                } else if (level <= 5 && level >= 0){
                                    if (new ItemIntensify().validate(tool)) {
                                        opertor.intensify();
                                        makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                    } else if (new ItemLuckyIntensify().validate(tool)) {
                                        opertor.intensify();
                                        makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                    } else if (new ItemSafetyIntensify().validate(tool)) {
                                        opertor.intensify();
                                        makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                    }
                                }
                            }
                        }
                    } else {
                        if (new ItemBottle().validate(tool)) {
                            //熔炼
                            makerUI.progress(inv,opertor.extract().getItemStack(),p);
                        } else {
                            if (new ItemIdentify().validate(tool)) {
                                //鉴定
                                if (opertor.identify()) {
                                    makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                } else {
                                    p.sendMessage(ChatColor.RED + "鉴定失败,请检查武器属性");
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "请放入正确的锻造工具与鉴定后的武器,并保持锻造工具在前武器在后的顺序");
                            }
                        }
                    }
                }
            } else {
                ItemStack putIS = e.getCursor();
                if (putIS == null || Material.AIR == putIS.getType()) return;
                if (slot == 0 && Material.NETHER_STAR == putIS.getType()) {
                    if (new ItemReborn().validate(putIS)) {
                        inv.setItem(1,new ItemStack(Material.AIR));
                        p.sendMessage(ChatColor.GOLD + "检测到转生石放入，可在转生石后一格放入核心来附加在武器中");
                    }
                }
            }
        }
    }
}
