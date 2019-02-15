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
public class SetCommand implements ICommandHandler {

    @Override
    public boolean handle(GamePlayer gamePlayer, String args[]) {
        if (args.length > 1) {
            String arenaName = args[0];
            String locationType = args[1];
            MessageSender messageSender = MessageSender.getINSTANCE();
            Arena arena = ArenaManager.getInstance().getArena(arenaName);
            Location up,down;

            if (arena == null) {
                messageSender.echo(gamePlayer,ChatColor.RED+"ָ�����ز�����");
                return true;
            }
            up = PlayerInteractHandler.getUp();
            down = PlayerInteractHandler.getDown();
            if (up == null || down == null) {
                messageSender.echo(gamePlayer, ChatColor.RED+"ȱ��λ��");
                return true;
            }
            switch (locationType) {
                case "main":
                    arena.setMainLocation(up,down);
                    messageSender.echo(gamePlayer, ChatColor.GREEN+"������λ���趨���");
                    break;
                case "sub":
                    arena.setSubLocation(up,down);
                    messageSender.echo(gamePlayer, ChatColor.GREEN+"������λ���趨���");
                    break;
                case "maintp":
                    arena.setMainTp(down);
                    messageSender.echo(gamePlayer, ChatColor.GREEN+"�����ش��͵�λ���趨���");
                    break;
                case "subtp":
                    arena.setSubTp(down);
                    messageSender.echo(gamePlayer, ChatColor.GREEN+"�����ش��͵�λ���趨���");
                    break;
            }
        }
        return true;
    }
}
