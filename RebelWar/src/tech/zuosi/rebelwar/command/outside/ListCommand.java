package tech.zuosi.rebelwar.command.outside;

import org.bukkit.ChatColor;
import tech.zuosi.rebelwar.command.ICommandHandler;
import tech.zuosi.rebelwar.game.manager.ArenaManager;
import tech.zuosi.rebelwar.game.manager.QueueManager;
import tech.zuosi.rebelwar.game.object.Arena;
import tech.zuosi.rebelwar.game.object.GameConfig;
import tech.zuosi.rebelwar.game.object.GamePlayer;
import tech.zuosi.rebelwar.message.MessageSender;

import java.util.Map;

/**
 * Created by iwar on 2016/10/1.
 */
public class ListCommand implements ICommandHandler {

    @Override
    public boolean handle(GamePlayer gamePlayer, String args[]) {
        // Format
        MessageSender messageSender = MessageSender.getINSTANCE();
        Map<String,QueueManager.State> map = QueueManager.getINSTANCE().getStates();
        Map<String,Arena> allArena = ArenaManager.getInstance().getArenaMap();
        allArena.keySet().removeAll(map.keySet());
        if (map.size()+allArena.keySet().size() == 0)
            messageSender.echo(gamePlayer, ChatColor.GOLD+"ɶ��û��");
        for (Map.Entry<String,QueueManager.State> entry:map.entrySet()) {
            String str = ChatColor.GRAY + "�ȴ���";
            String arenaName = entry.getKey();
            switch (entry.getValue()) {
                case WAIT:
                    str += " [{current}/{max}]"
                            .replace("{current}",String.valueOf(QueueManager.getINSTANCE().showNumInfo(arenaName)))
                            .replace("{max}", String.valueOf(GameConfig.getInstance().getMEMBERLIMIT()));
                    break;
                case READY:
                    str = ChatColor.GREEN + "׼����";
                    break;
                case START:
                    str = ChatColor.RED + "��Ϸ��";
                    break;
            }
            messageSender.echo(gamePlayer, ChatColor.GOLD+"["+arenaName+"]"+ChatColor.BOLD+" -> "+str);
        }
        for (String arenaName:allArena.keySet()) {
            messageSender.echo(gamePlayer, ChatColor.GOLD+"["+arenaName+"]"+ChatColor.BOLD +" -> "
                    +ChatColor.UNDERLINE + ChatColor.DARK_RED + "δ���");
        }
        return true;
    }
}
