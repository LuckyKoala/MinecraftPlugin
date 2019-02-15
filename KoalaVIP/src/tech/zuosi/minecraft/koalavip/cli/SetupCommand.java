package tech.zuosi.minecraft.koalavip.cli;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import tech.zuosi.minecraft.koalavip.Core;

/**
 * Created by luckykoala on 18-4-3.
 */
public class SetupCommand implements SubCommandExecutor {

    private void sendHelpMessage(CommandSender sender) {
        sender.sendMessage(new String[]{
                ChatColor.BLUE+"==="+ChatColor.WHITE+"KoalaVIP Setup帮助菜单"+ChatColor.BLUE+"===",
                ChatColor.GOLD+"/kvip setup [database/prefix/username/password/port] <value>:"
                        +ChatColor.WHITE+" 设置相应的数据库配置项",
                ChatColor.GOLD+"/kvip setup info"+ChatColor.WHITE+" 查看当前数据库配置信息",
                ChatColor.GOLD+"/kvip setup done"+ChatColor.WHITE+" 以当前配置启动数据库管理器",
        });
    }

    @Override
    public boolean onCommand(CommandSender sender, String[] args) {
        if(!sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "本命令仅限OP执行");
            return true;
        }

        if(args.length == 0) {
            sendHelpMessage(sender);
        } else if(args.length == 1) {
            if("info".equalsIgnoreCase(args[0])) {
                //显示数据库配置信息
                ConfigurationSection databaseSection = Core.getInstance().getConfig().getConfigurationSection("Database");
                String username = databaseSection.getString("username");
                String password = databaseSection.getString("password");
                String database = databaseSection.getString("database");
                int port = databaseSection.getInt("port", 3306);
                String prefix = databaseSection.getString("prefix", "");

                sender.sendMessage(ChatColor.WHITE +
                        String.format("用户名: %s\n密码： %s\n端口: %d\n数据库名: %s\n表前缀: %s",
                                username, password, port, database, prefix));
            } else if("done".equalsIgnoreCase(args[0])) {
                Core.getInstance().getConfig().set("setup", false);
                Core.getInstance().loadDatabaseConfig();
            }
        } else {
            String key = args[0];
            String valueStr = args[1];

            switch (key.toLowerCase()) {
                case "database":
                    Core.getInstance().getConfig().getConfigurationSection("Database").set("database", valueStr);
                    sender.sendMessage(ChatColor.GREEN + "已成功设置数据库名为"+valueStr);
                    break;
                case "prefix":
                    Core.getInstance().getConfig().getConfigurationSection("Database").set("prefix", valueStr);
                    sender.sendMessage(ChatColor.GREEN + "已成功设置表前缀为"+valueStr);
                    break;
                case "username":
                    Core.getInstance().getConfig().getConfigurationSection("Database").set("username", valueStr);
                    sender.sendMessage(ChatColor.GREEN + "已成功设置用户名为"+valueStr);
                    break;
                case "password":
                    Core.getInstance().getConfig().getConfigurationSection("Database").set("password", valueStr);
                    sender.sendMessage(ChatColor.GREEN + "已成功设置密码为"+valueStr);
                    break;
                case "port":
                    try {
                        int value = Integer.parseInt(valueStr);
                        Core.getInstance().getConfig().getConfigurationSection("Database").set("port", value);
                        sender.sendMessage(ChatColor.GREEN + "已成功设置端口为"+value);
                    } catch (NumberFormatException ex) {
                        sender.sendMessage(ChatColor.RED + "端口值仅限数字，请重新设置");
                    }
                    break;
                default:
                    sender.sendMessage(ChatColor.RED + "无法识别的选项，请重新输入");
                    break;
            }
        }

        return true;
    }
}
