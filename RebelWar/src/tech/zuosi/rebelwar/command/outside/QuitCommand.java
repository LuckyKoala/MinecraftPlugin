package tech.zuosi.rebelwar.command.outside;

import org.bukkit.ChatColor;
import tech.zuosi.rebelwar.command.ICommandHandler;
import tech.zuosi.rebelwar.game.manager.QueueManager;
import tech.zuosi.rebelwar.game.object.GameConfig;
import tech.zuosi.rebelwar.game.object.GamePlayer;
import tech.zuosi.rebelwar.message.MessageSender;
import tech.zuosi.rebelwar.util.BCChannel;

/**
 * Created by iwar on 2016/10/1.
 */
public class QuitCommand implements ICommandHandler {

    @Override
    public boolean handle(GamePlayer gamePlayer, String args[]) {
        MessageSender messageSender = MessageSender.getINSTANCE();
        if (QueueManager.getINSTANCE().isInStartedGame(gamePlayer)) {
            gamePlayer.getCurrentGame().getCurrentManager().quit(gamePlayer);
        } else {
            //检测是否在队列中
            if (QueueManager.getINSTANCE().isWaitInQueue(gamePlayer)) {
                messageSender.echo(gamePlayer,ChatColor.RED+"您仍在队列中，请先输入/rebel leave命令离开队列");
            } else {
                //不在队列中则返回lobby服务器
                messageSender.echo(gamePlayer, ChatColor.BLUE+"并没有处于进行中的游戏，故返回大厅服务器");
                BCChannel.teleportPlayerTo(gamePlayer.getPlayer(), GameConfig.getLobbyServerName());
            }
        }

        return true;
    }
}
