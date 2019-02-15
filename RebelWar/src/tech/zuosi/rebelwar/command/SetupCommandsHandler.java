package tech.zuosi.rebelwar.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.zuosi.rebelwar.RebelWar;
import tech.zuosi.rebelwar.command.arena.ArenaCommand;
import tech.zuosi.rebelwar.command.outside.*;
import tech.zuosi.rebelwar.game.manager.PlayerManager;
import tech.zuosi.rebelwar.game.object.GamePlayer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/10/1.
 */
public class SetupCommandsHandler implements CommandExecutor {
    private Map<String,ICommandHandler> handlerMap = new HashMap<>();

    public SetupCommandsHandler() {
        this.handlerMap.put("arena",new ArenaCommand());
        this.handlerMap.put("join",new JoinCommand());
        this.handlerMap.put("leave",new LeaveCommand());
        this.handlerMap.put("list",new ListCommand());
        this.handlerMap.put("pro",new ProCommand());
        this.handlerMap.put("quit",new QuitCommand());
        this.handlerMap.put("shop",new ShopCommand());
        this.handlerMap.put("stat",new StatCommand());
    }

    //Ȩ�޼��
    //����ϸ�ķֲ㣬���������� null,exist
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!cmd.getName().equalsIgnoreCase("rebel")) return false;
        if (!(sender instanceof Player)) {
            sender.sendMessage("����������ʵ��ʹ��.");
            return false;
        } else if (args.length > 0 && this.handlerMap.containsKey(args[0])) {
            GamePlayer gamePlayer = PlayerManager.getInstance().getGamePlayer(sender.getName());
            boolean result = this.handlerMap.get(args[0]).handle(gamePlayer, Arrays.copyOfRange(args, 1, args.length));
            return result;
        } else if (args.length==0) {
            Player player = (Player) sender;
            showAvailableCmd(player,player.hasPermission("rebelwar.arena"));
            return true;
        } else {
            return false;
        }
    }

    private void showAvailableCmd(Player player,boolean hasPermission) {
        player.sendMessage(new String[]{
                ChatColor.BLUE+"Version:"+ RebelWar.getINSTANCE().getDescription().getVersion(),
                ChatColor.GOLD+"/rebel join <name>         "+ChatColor.GRAY+"--->"+ChatColor.GREEN+"      ������Ϸ����",
                ChatColor.GOLD+"/rebel leave               "+ChatColor.GRAY+"--->"+ChatColor.GREEN+"      �뿪��Ϸ����",
                ChatColor.GOLD+"/rebel list                "+ChatColor.GRAY+"--->"+ChatColor.GREEN+"      �г����ж�����Ϣ",
                ChatColor.GOLD+"/rebel pro <ְҵ����>      "+ChatColor.GRAY+"--->"+ChatColor.GREEN+"       �鿴/�л� Ĭ��ְҵ",
                ChatColor.GOLD+"/rebel quit                "+ChatColor.GRAY+"--->"+ChatColor.GREEN+"      �˳����ڽ��е���Ϸ", //half
                ChatColor.GOLD+"/rebel stat                "+ChatColor.GRAY+"--->"+ChatColor.GREEN+"      ��ʾ��������",
                ChatColor.GOLD+"/rebel shop <list/buy> <id>  "+ChatColor.GRAY+"--->"+ChatColor.GREEN+"      �о�/���� �̵���Ʒ"
        });
        if (hasPermission) {
            player.sendMessage(ChatColor.GOLD
                    + "/rebel arena <add/set/del> <����> <main/sub/chest/maintp/subtp>" + '\n'
                    + ChatColor.GREEN + "���/�Ƴ� ��Ϸ���� ���� ���ó���λ�� ���� ���/ɾ������λ��");
        }
    }
}
