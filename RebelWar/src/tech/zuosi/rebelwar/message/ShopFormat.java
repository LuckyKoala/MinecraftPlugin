package tech.zuosi.rebelwar.message;

import org.bukkit.ChatColor;
import tech.zuosi.rebelwar.game.object.GamePlayer;

/**
 * Created by iwar on 2016/10/1.
 */
public class ShopFormat {

    //展示可以购买的物品列表
    public void showItemList(GamePlayer gamePlayer) {
        MessageSender.getINSTANCE().echo(gamePlayer,new String[]{
                ChatColor.BOLD+"                          商店物品",
                ChatColor.GREEN+"[0]步兵->铁套[保护I]，石剑                                 "+ChatColor.GOLD+"500游戏币",
                ChatColor.GREEN+"[1]侦察兵->皮革套[保护V] 弓[力量II] 弓箭x30 石剑 永久速度II  "+ChatColor.GOLD+"800游戏币",
                ChatColor.GREEN+"[2]坦克->钻石甲[保护I] 铁剑[锋利I]                          "+ChatColor.GOLD+"1000游戏币",
                ChatColor.GREEN+"[3]战士->铁套[保护II] 钻石剑[锋利I]                         "+ChatColor.GOLD+"1200游戏币",
                ChatColor.GREEN+"[4]战神->钻石甲保护[III] 钻石剑[锋利II]                     "+ChatColor.GOLD+"2000游戏币",
                ChatColor.GREEN+"[5]物品->面包x5                                            "+ChatColor.GOLD+"10游戏币",
                ChatColor.GREEN+"[6]物品->弓箭x10                                          "+ChatColor.GOLD+"10游戏币",
                ChatColor.GREEN+"[7]物品->金苹果x1                                         "+ChatColor.GOLD+"50游戏币",
                ChatColor.GREEN+"[8]物品->迅捷药水II(喷溅)                                  "+ChatColor.GOLD+"100游戏币",
                ChatColor.GREEN+"[9]物品->末影珍珠x5                                        "+ChatColor.GOLD+"200游戏币",
                ChatColor.UNDERLINE+"输入/rebel shop buy <id>购买对应物品"
        });
    }
}
