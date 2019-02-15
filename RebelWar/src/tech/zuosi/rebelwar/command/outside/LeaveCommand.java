package tech.zuosi.rebelwar.command.outside;

import org.bukkit.ChatColor;
import tech.zuosi.rebelwar.command.ICommandHandler;
import tech.zuosi.rebelwar.game.manager.QueueManager;
import tech.zuosi.rebelwar.game.object.GamePlayer;
import tech.zuosi.rebelwar.message.MessageSender;

/**
 * Created by iwar on 2016/10/1.
 */
public class LeaveCommand implements ICommandHandler {

    @Override
    public boolean handle(GamePlayer gamePlayer, String args[]) {
        MessageSender messageSender = MessageSender.getINSTANCE();
        int status = QueueManager.getINSTANCE().leave(gamePlayer);
        switch (status) {
            case -1:
                messageSender.echo(gamePlayer, ChatColor.RED+"��û�м����κζ���");
                break;
            case 0:
                messageSender.echo(gamePlayer, ChatColor.RED+"��Ϸ�Ѿ���ʼ��������/rebel quit�˳���Ϸ");
                break;
            case 1:
                messageSender.echo(gamePlayer, ChatColor.GREEN+"�ɹ��˳�����");
                break;
        }

        return true;
    }
}
