package tech.zuosi.rebelwar.command.outside;

import org.bukkit.ChatColor;
import tech.zuosi.rebelwar.command.ICommandHandler;
import tech.zuosi.rebelwar.game.manager.QueueManager;
import tech.zuosi.rebelwar.game.object.GamePlayer;
import tech.zuosi.rebelwar.message.MessageSender;

/**
 * Created by iwar on 2016/10/1.
 */
public class JoinCommand implements ICommandHandler {

    @Override
    public boolean handle(GamePlayer gamePlayer, String args[]) {
        MessageSender messageSender = MessageSender.getINSTANCE();
        if (args.length == 0) {
            //快速加入
            String arenaName = QueueManager.getINSTANCE().quickJoin(gamePlayer);
            if ("".equals(arenaName)) {
                messageSender.echo(gamePlayer, ChatColor.RED+"找不到满足要求的队列");
                return true;
            } else {
                messageSender.echo(gamePlayer, ChatColor.GREEN+"成功加入队列["
                        +ChatColor.GOLD+arenaName+ChatColor.GREEN+"]");
                return true;
            }
        } else if (args.length == 1) {
            //指定加入
            String arenaName = args[0];
            int status = QueueManager.getINSTANCE().join(gamePlayer,arenaName);
            switch (status) {
                case -2:
                    messageSender.echo(gamePlayer, ChatColor.RED+"未知错误");
                    break;
                case -1:
                    messageSender.echo(gamePlayer, ChatColor.RED+"找不到指定队列，请检查输入的队列名称是否有误");
                    break;
                case 0:
                    messageSender.echo(gamePlayer, ChatColor.RED+"队列已满或已经开始游戏");
                    break;
                case 1:
                    messageSender.echo(gamePlayer, ChatColor.GREEN+"成功加入队列["
                            +ChatColor.GOLD+arenaName+ChatColor.GREEN+"]");
                    break;
            }

            return true;
        }
        return false;
    }
}
