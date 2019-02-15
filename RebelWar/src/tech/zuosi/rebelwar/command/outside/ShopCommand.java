package tech.zuosi.rebelwar.command.outside;

import org.bukkit.ChatColor;
import tech.zuosi.rebelwar.command.ICommandHandler;
import tech.zuosi.rebelwar.game.object.GamePlayer;
import tech.zuosi.rebelwar.game.object.GameShop;
import tech.zuosi.rebelwar.message.MessageSender;
import tech.zuosi.rebelwar.message.ShopFormat;

/**
 * Created by iwar on 2016/10/1.
 */
public class ShopCommand implements ICommandHandler {

    @Override
    public boolean handle(GamePlayer gamePlayer, String args[]) {
        MessageSender messageSender = MessageSender.getINSTANCE();
        GameShop gameShop = GameShop.getInstance();

        if (args.length >= 1) {
            String cmd = args[0];
            if ("list".equalsIgnoreCase(cmd)) {
                new ShopFormat().showItemList(gamePlayer);
                return true;
            } else if ("buy".equalsIgnoreCase(cmd)) {
                if (args.length >= 2) {
                    int index;
                    boolean result;

                    try {
                        index = Integer.parseInt(args[1]);
                    } catch (NumberFormatException ex) {
                        index = -1;
                    }
                    if (index > 9 || index < 0) {
                        messageSender.echo(gamePlayer,ChatColor.RED+"请输入正确的商品id");
                        return true;
                    }
                    switch (index) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                            result = gameShop.bindPlayer(gamePlayer)
                                    .purchaseProfession(GamePlayer.Profession.getProById(index));
                            if (!result) messageSender.echo(gamePlayer,ChatColor.RED+"购买失败，已经拥有该职业或是游戏币不足");
                            else messageSender.echo(gamePlayer,ChatColor.GREEN+"购买成功.");
                            return true;
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                            result = gameShop.purchaseSingleItem(GameShop.SingleItem.getSIById(index));
                            if (!result) messageSender.echo(gamePlayer,ChatColor.RED+"购买失败，游戏币不足");
                            else messageSender.echo(gamePlayer,ChatColor.GREEN+"购买成功.");
                            return true;
                    }
                } else {
                    messageSender.echo(gamePlayer, ChatColor.RED+"请输入对应商品id进行购买");
                    return true;
                }
            }
        }


        return false;
    }
}
