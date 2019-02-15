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
            //���ټ���
            String arenaName = QueueManager.getINSTANCE().quickJoin(gamePlayer);
            if ("".equals(arenaName)) {
                messageSender.echo(gamePlayer, ChatColor.RED+"�Ҳ�������Ҫ��Ķ���");
                return true;
            } else {
                messageSender.echo(gamePlayer, ChatColor.GREEN+"�ɹ��������["
                        +ChatColor.GOLD+arenaName+ChatColor.GREEN+"]");
                return true;
            }
        } else if (args.length == 1) {
            //ָ������
            String arenaName = args[0];
            int status = QueueManager.getINSTANCE().join(gamePlayer,arenaName);
            switch (status) {
                case -2:
                    messageSender.echo(gamePlayer, ChatColor.RED+"δ֪����");
                    break;
                case -1:
                    messageSender.echo(gamePlayer, ChatColor.RED+"�Ҳ���ָ�����У���������Ķ��������Ƿ�����");
                    break;
                case 0:
                    messageSender.echo(gamePlayer, ChatColor.RED+"�����������Ѿ���ʼ��Ϸ");
                    break;
                case 1:
                    messageSender.echo(gamePlayer, ChatColor.GREEN+"�ɹ��������["
                            +ChatColor.GOLD+arenaName+ChatColor.GREEN+"]");
                    break;
            }

            return true;
        }
        return false;
    }
}
