package tech.zuosi.minecraft.parkour.handler.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import tech.zuosi.minecraft.parkour.Core;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static tech.zuosi.minecraft.parkour.game.MapPath.PATH_FORMAT;

/**
 * Created by LuckyKoala on 18-9-17.
 */
public class TopCommandExecutor implements CommandExecutor, TabCompleter {
    static final String TOP_COMMAND_LABEL = "/po";

    private static final Map<String, CommandDispatcher> subCommandMap = new HashMap<String, CommandDispatcher>() {{
        put("create", new CreateCommand());
        put("edit", new EditCommand());
        put("join", new JoinCommand());
        put("list", new ListCommand());
        put("leave", new LeaveCommand());
        put("menu", new MenuCommand());
        put("option", new OptionCommand());
        put("sync", new SyncCommand());
    }};

    //Attention: Param 'label' in CommandDispatcher.onCommand is subCmd's label
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if("po".equals(label)) {
            if(args.length == 0) {
                displayHelpInfo(sender);
            } else {
                String subCmd = args[0];
                CommandDispatcher dispatcher = subCommandMap.get(subCmd);
                if(dispatcher != null) {
                    dispatcher.onCommand(sender, cmd, subCmd, Arrays.copyOfRange(args, 1, args.length));
                } else {
                    displayHelpInfo(sender);
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if("po".equals(cmd.getLabel())) {
            if(args.length == 1) {
                String subCmd = args[0];
                return subCommandMap.keySet().stream()
                        .filter(str -> str.startsWith(subCmd))
                        .collect(Collectors.toList());
            } else if (args.length > 1) {
                String subCmd = args[0];
                CommandDispatcher dispatcher = subCommandMap.get(subCmd);
                if(dispatcher != null) {
                    return dispatcher.onTabComplete(sender, cmd, alias, Arrays.copyOfRange(args, 1, args.length));
                }
            }
        }
        return null;
    }

    public void displayHelpInfo(CommandSender sender) {
        sender.sendMessage(new String[]{
                ChatColor.BLUE+"==="+ChatColor.WHITE+"ParkOur帮助菜单"+ChatColor.BLUE+"===",
                ChatColor.YELLOW+"支持使用Tab补全命令",
                ChatColor.YELLOW+"使用不带参数的/po list命令可以快速选取地图",
                ChatColor.GOLD+TOP_COMMAND_LABEL+ChatColor.WHITE+": 查看帮助信息",
                ChatColor.GOLD+TOP_COMMAND_LABEL+" menu:"+ChatColor.WHITE+" 打开地图菜单",
                ChatColor.GOLD+TOP_COMMAND_LABEL+" join <"+PATH_FORMAT +">:"+ChatColor.WHITE+" 加入游戏",
                ChatColor.GOLD+TOP_COMMAND_LABEL+" leave:"+ChatColor.WHITE+" 离开游戏",
                ChatColor.GOLD+TOP_COMMAND_LABEL+" list ["+PATH_FORMAT+"]:"+ChatColor.WHITE+" 列出可选项,参数可省略",
        });
        if(sender.isOp()) {
            sender.sendMessage(new String[]{
                    ChatColor.GOLD+TOP_COMMAND_LABEL+" create <"+PATH_FORMAT +">:"+ChatColor.WHITE+" 创建地图",
                    ChatColor.GOLD+TOP_COMMAND_LABEL+" edit <"+PATH_FORMAT +">:"+ChatColor.WHITE+" 编辑地图",
                    ChatColor.GOLD+TOP_COMMAND_LABEL+" sync <"+PATH_FORMAT +">:"+ChatColor.WHITE+" 将对应地图的默认格式数据写入配置文件",
                    ChatColor.DARK_GRAY+"当前插件版本号: "+ChatColor.GREEN+ Core.getInstance().getDescription().getVersion(),
            });
        }
    }
}
