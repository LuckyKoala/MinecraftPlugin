package tech.zuosi.rebelwar.command.arena;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import tech.zuosi.rebelwar.command.ICommandHandler;
import tech.zuosi.rebelwar.game.manager.ArenaManager;
import tech.zuosi.rebelwar.game.object.Arena;
import tech.zuosi.rebelwar.game.object.GamePlayer;
import tech.zuosi.rebelwar.handler.PlayerInteractHandler;
import tech.zuosi.rebelwar.message.MessageSender;

/**
 * Created by iwar on 2016/10/1.
 */
public class DelCommand implements ICommandHandler {

    @Override
    public boolean handle(GamePlayer gamePlayer, String args[]) {
        MessageSender messageSender = MessageSender.getINSTANCE();
        if (args.length == 1) {
            boolean result = ArenaManager.getInstance().delArena(args[0]);
            if (!result) messageSender.echo(gamePlayer, ChatColor.RED+"�ó��ز�������");
            else messageSender.echo(gamePlayer, ChatColor.GREEN+"�ɹ�ɾ���ó���");
            return true;
        } else if (args.length >= 2 && "chest".equalsIgnoreCase(args[1])) {
            Arena arena = ArenaManager.getInstance().getArena(args[0]);
            Location loc = PlayerInteractHandler.getDown();
            if (arena == null) {
                messageSender.echo(gamePlayer, ChatColor.RED+"ָ���ĳ��ز����ڣ����ȴ�����Ӧ����");
                return true;
            } else if (loc == null) {
                messageSender.echo(gamePlayer, ChatColor.RED+"����ѡ��λ��");
                return true;
            }
            arena.removeChestLocation(loc);
            messageSender.echo(gamePlayer, ChatColor.GOLD+"ɾ������λ��ɹ�");
            return true;
        }
        return false;
    }
}
