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
        if ("����̨".equals(inv.getTitle())) {
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
                                    ChatColor.GOLD + "������̨֧�����¹���",
                                    ChatColor.GREEN + "������ǿ������Ƕ��ת������ף�����",
                                    ChatColor.RED + "ת�������ڹ��ߴ�����ת��ʯ��֮���ѡ���ڵڶ���������"
                            }).initData("PANE", ItemType.INFO,new String[]{"0","0","0"})
                                    .setDisplayName(ChatColor.YELLOW + "�������").getItemStack());
                            p.sendMessage(ChatColor.GOLD + "��⵽ת��ʯ�Ƴ����ѻָ��������ģʽ");
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
                        p.sendMessage(ChatColor.RED + "�Ƿ��Ĺ���.");
                    }
                    if (Validate.isOperateLegal(util)) {
                        //���
                        if (new ItemDrill().validate(tool)) {
                            if (opertor.drill()) {
                                makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                            } else {
                                p.sendMessage(ChatColor.RED + "���ʧ��,������������");
                            }
                        } else if (new ItemGem().validate(tool)) {
                            //��Ƕ
                            if (opertor.inlay(new ItemGem(tool))) {
                                makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                            } else {
                                p.sendMessage(ChatColor.RED + "��Ƕʧ��,��ȷ�������пյ���Ƕ������Ƕ����û��ͬ�౦ʯ����");
                            }
                        } else {
                            if (Material.STAINED_GLASS_PANE != safeMaterial(core)) {
                                if (Material.AIR == safeMaterial(core)) {
                                    int level = ((int)util.getItemData("intensifyLevel"));
                                    if (level != 10) {
                                        p.sendMessage(ChatColor.RED + "ת��ʧ��,����ǿ���ȼ�δ�ﵽʮ���������ǿ��");
                                        return;
                                    }
                                    opertor.reborn();
                                    makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                } else if (new ItemCore().validate(core)) {
                                    //ת��
                                    int level = ((int)util.getItemData("intensifyLevel"));
                                    if (level != 10) {
                                        p.sendMessage(ChatColor.RED + "ת��ʧ��,����ǿ���ȼ�δ�ﵽʮ���������ǿ��");
                                        return;
                                    }
                                    CoreType coreType = new ItemCore(core).getCoreType();
                                    if (opertor.reborn(coreType)) {
                                        makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                    } else {
                                        p.sendMessage(ChatColor.RED + "ת��ʧ�ܣ����������Ƿ����к���");
                                    }
                                }
                            } else {
                                //ǿ��
                                int level = ((int)util.getItemData("intensifyLevel"));
                                if (level > 5 && level <= 10) {
                                    if (level == 10) {
                                        p.sendMessage(ChatColor.RED + "ǿ��ʧ��,����ǿ���ȼ��޷�����ߣ��볢��ת��");
                                        return;
                                    }
                                    if (new ItemIntensify().validate(tool)) {
                                        if (opertor.intensify(KoalaItem.INSTANCE.getConfig().getInt("Chance.intensify"),false)) {
                                            makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                        } else {
                                            makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                            p.sendMessage(ChatColor.RED + "ǿ��ʧ��,��������");
                                        }
                                    } else if (new ItemLuckyIntensify().validate(tool)) {
                                        if (opertor.intensify(KoalaItem.INSTANCE.getConfig().getInt("Chance.lucky"),false)) {
                                            makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                        } else {
                                            makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                            p.sendMessage(ChatColor.RED + "ǿ��ʧ��,��������");
                                        }
                                    } else if (new ItemSafetyIntensify().validate(tool)) {
                                        if (opertor.intensify(KoalaItem.INSTANCE.getConfig().getInt("Chance.intensify"),true)) {
                                            makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                        } else {
                                            makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                            p.sendMessage(ChatColor.RED + "ǿ��ʧ��,��������ԭ��");
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
                            //����
                            makerUI.progress(inv,opertor.extract().getItemStack(),p);
                        } else {
                            if (new ItemIdentify().validate(tool)) {
                                //����
                                if (opertor.identify()) {
                                    makerUI.progress(inv,util.updateDisplayData().getItemStack(),p);
                                } else {
                                    p.sendMessage(ChatColor.RED + "����ʧ��,������������");
                                }
                            } else {
                                p.sendMessage(ChatColor.RED + "�������ȷ�Ķ��칤��������������,�����ֶ��칤����ǰ�����ں��˳��");
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
                        p.sendMessage(ChatColor.GOLD + "��⵽ת��ʯ���룬����ת��ʯ��һ����������������������");
                    }
                }
            }
        }
    }
}
