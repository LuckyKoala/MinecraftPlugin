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
            //����Ƿ��ڶ�����
            if (QueueManager.getINSTANCE().isWaitInQueue(gamePlayer)) {
                messageSender.echo(gamePlayer,ChatColor.RED+"�����ڶ����У���������/rebel leave�����뿪����");
            } else {
                //���ڶ������򷵻�lobby������
                messageSender.echo(gamePlayer, ChatColor.BLUE+"��û�д��ڽ����е���Ϸ���ʷ��ش���������");
                BCChannel.teleportPlayerTo(gamePlayer.getPlayer(), GameConfig.getLobbyServerName());
            }
        }

        return true;
    }
}
