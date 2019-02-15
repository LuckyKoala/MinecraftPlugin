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
            player.sendMessage(ChatColor.RED + "缺少权限rebelwar.arena");
            return true;
        }
        if (args.length > 0 && this.handlerMap.containsKey(args[0])) {
            return this.handlerMap.get(args[0]).handle(gamePlayer, Arrays.copyOfRange(args, 1, args.length));
        } else {
            player.sendMessage(new String[]{
                    ChatColor.GOLD+"场地设置帮助:\n1./rebel arena add <名称>"+ChatColor.GRAY+"创建新的竞技场",
                    ChatColor.GOLD+"2./rebel arena set <名称> main"+ChatColor.GRAY+"为竞技场设置第一阶段的场地，使用木斧选择对角线两点后输入此命令",
                    ChatColor.GOLD+"3./rebel arena set <名称> sub"+ChatColor.GRAY+"为竞技场设置第二阶段的场地，使用木斧选择对角线两点后输入此命令",
                    ChatColor.GOLD+"4./rebel arena set <名称> maintp"+ChatColor.GRAY+"为竞技场设置第一阶段的传送点，使用木斧左键选取一点后输入此命令",
                    ChatColor.GOLD+"5./rebel arena set <名称> subtp"+ChatColor.GRAY+"为竞技场设置第二阶段的传送点，使用木斧左键选取一点后输入此命令",
                    ChatColor.GOLD+"6./rebel arena add <名称> chest"+ChatColor.GRAY+"为竞技场添加一个箱子点，使用木斧左键选取一点后输入此命令",
                    ChatColor.GOLD+"7./rebel arena del <名称> chest"+ChatColor.GRAY+"为竞技场移除一个箱子点，使用木斧左键选取一点后输入此命令\n箱子点至少10个",
                    ChatColor.GOLD+"8./rebel arena del <名称>"+ChatColor.GRAY+"删除已有的竞技场",
                    ChatColor.GOLD+"9./rebel list"+ChatColor.GRAY+"查看队列中竞技场的情况，显示等待中则表示已经设置完毕",
            });
        }
        return true;
    }
}
