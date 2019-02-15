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
            if (!result) messageSender.echo(gamePlayer, ChatColor.RED+"该场地并不存在");
            else messageSender.echo(gamePlayer, ChatColor.GREEN+"成功删除该场地");
            return true;
        } else if (args.length >= 2 && "chest".equalsIgnoreCase(args[1])) {
            Arena arena = ArenaManager.getInstance().getArena(args[0]);
            Location loc = PlayerInteractHandler.getDown();
            if (arena == null) {
                messageSender.echo(gamePlayer, ChatColor.RED+"指定的场地不存在，请先创建对应场地");
                return true;
            } else if (loc == null) {
                messageSender.echo(gamePlayer, ChatColor.RED+"请先选定位标");
                return true;
            }
            arena.removeChestLocation(loc);
            messageSender.echo(gamePlayer, ChatColor.GOLD+"删除箱子位标成功");
            return true;
        }
        return false;
    }
}
