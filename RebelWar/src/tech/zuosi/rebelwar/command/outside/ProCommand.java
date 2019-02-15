package tech.zuosi.rebelwar.command.outside;

import org.bukkit.ChatColor;
import tech.zuosi.rebelwar.command.ICommandHandler;
import tech.zuosi.rebelwar.game.object.GamePlayer;
import tech.zuosi.rebelwar.message.MessageSender;

import java.util.Set;

/**
 * Created by iwar on 2016/10/1.
 */
public class ProCommand implements ICommandHandler {

    @Override
    public boolean handle(GamePlayer gamePlayer, String args[]) {
        MessageSender messageSender = MessageSender.getINSTANCE();
        Set<GamePlayer.Profession> professions = gamePlayer.getProfessions();
        GamePlayer.Profession defaultProfession = gamePlayer.getDefaultProfession();
        if (args.length == 0) {
            if (professions.size() == 0) {
                messageSender.echo(gamePlayer, ChatColor.RED+"没有购买的职业");
            } else {
                for (GamePlayer.Profession pro:professions) {
                    messageSender.echo(gamePlayer
                            , ChatColor.GREEN+"[name]description".replace("name",pro.name()
                                    .replace("description",pro.getDescription())));
                }
            }
            messageSender.echo(gamePlayer
                    ,ChatColor.GRAY+"默认职业为[name]description".replace("name",defaultProfession.name())
                            .replace("description",defaultProfession.getDescription()));
        } else if (args.length >= 1) {
            GamePlayer.Profession tarProfession = null;
            try {
                tarProfession = GamePlayer.Profession.valueOf(args[1]);
            } catch (IllegalArgumentException ignored) {}

            if (tarProfession == null) {
                messageSender.echo(gamePlayer,ChatColor.RED+"找不到指定职业");
            } else {
                if (professions.size() == 0) {
                    messageSender.echo(gamePlayer,ChatColor.RED+"没有购买的职业以供切换");
                } else {
                    if (professions.contains(tarProfession)) {
                        gamePlayer.setDefaultProfession(tarProfession);
                    } else {
                        messageSender.echo(gamePlayer,ChatColor.RED+"该职业未购买");
                    }
                }
            }
        }

        return true;
    }
}
