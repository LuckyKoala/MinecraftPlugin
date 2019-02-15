package tech.zuosi.rebelwar.command.arena;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tech.zuosi.rebelwar.command.ICommandHandler;
import tech.zuosi.rebelwar.game.object.GamePlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/10/1.
 */
public class ArenaCommand implements ICommandHandler {
    private Map<String,ICommandHandler> handlerMap = new HashMap<>();

    public ArenaCommand() {
        this.handlerMap.put("add",new AddCommand());
        this.handlerMap.put("del",new DelCommand());
        this.handlerMap.put("set",new SetCommand());
    }

    @Override
    public boolean handle(GamePlayer gamePlayer, String args[]) {
        Player player = gamePlayer.getPlayer();
        if (!player.hasPermission("rebelwar.arena")) {
            player.sendMessage(ChatColor.RED + "ȱ��Ȩ��rebelwar.arena");
            return true;
        }
        if (args.length > 0 && this.handlerMap.containsKey(args[0])) {
            return this.handlerMap.get(args[0]).handle(gamePlayer, Arrays.copyOfRange(args, 1, args.length));
        } else {
            player.sendMessage(new String[]{
                    ChatColor.GOLD+"�������ð���:\n1./rebel arena add <����>"+ChatColor.GRAY+"�����µľ�����",
                    ChatColor.GOLD+"2./rebel arena set <����> main"+ChatColor.GRAY+"Ϊ���������õ�һ�׶εĳ��أ�ʹ��ľ��ѡ��Խ�����������������",
                    ChatColor.GOLD+"3./rebel arena set <����> sub"+ChatColor.GRAY+"Ϊ���������õڶ��׶εĳ��أ�ʹ��ľ��ѡ��Խ�����������������",
                    ChatColor.GOLD+"4./rebel arena set <����> maintp"+ChatColor.GRAY+"Ϊ���������õ�һ�׶εĴ��͵㣬ʹ��ľ�����ѡȡһ������������",
                    ChatColor.GOLD+"5./rebel arena set <����> subtp"+ChatColor.GRAY+"Ϊ���������õڶ��׶εĴ��͵㣬ʹ��ľ�����ѡȡһ������������",
                    ChatColor.GOLD+"6./rebel arena add <����> chest"+ChatColor.GRAY+"Ϊ���������һ�����ӵ㣬ʹ��ľ�����ѡȡһ������������",
                    ChatColor.GOLD+"7./rebel arena del <����> chest"+ChatColor.GRAY+"Ϊ�������Ƴ�һ�����ӵ㣬ʹ��ľ�����ѡȡһ������������\n���ӵ�����10��",
                    ChatColor.GOLD+"8./rebel arena del <����>"+ChatColor.GRAY+"ɾ�����еľ�����",
                    ChatColor.GOLD+"9./rebel list"+ChatColor.GRAY+"�鿴�����о��������������ʾ�ȴ������ʾ�Ѿ��������",
            });
        }
        return true;
    }
}
