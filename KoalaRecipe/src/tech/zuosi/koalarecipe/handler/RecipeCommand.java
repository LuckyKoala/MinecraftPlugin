package tech.zuosi.koalarecipe.handler;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import tech.zuosi.koalarecipe.KoalaRecipe;
import tech.zuosi.koalarecipe.recipe.RecipeFile;

/**
 * Created by iwar on 2016/8/23.
 */
public class RecipeCommand implements CommandExecutor {
    //TODO recipe bind <timestamp> <name> ��Ч�� recipe create <name>

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ("recipe".equalsIgnoreCase(label)) {
            if (args.length == 0) {
                sender.sendMessage(new String[]{
                        ChatColor.RED + "Version: " + ChatColor.GRAY + KoalaRecipe.getInstance().getDescription().getVersion(),
                        ChatColor.GREEN + "/recipe create " + ChatColor.AQUA + "�����µĺϳɱ�",
                        ChatColor.GREEN + "/recipe craft " + ChatColor.AQUA + "�ϳ���Ʒ",
                        ChatColor.GREEN + "/recipe update " + ChatColor.AQUA + "�����䷽�ļ�",
                        ChatColor.GREEN + "/recipe debug " + ChatColor.AQUA + "����Debugģʽ�������ں�̨�����Ϣ"
                });

                return true;
            }
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length >= 1) {
                    if ("create".equalsIgnoreCase(args[0])) {
                        if (!player.hasPermission("koalarecipe.create")) {
                            player.sendMessage(ChatColor.RED + "ȱ��Ȩ��<koalarecipe.create>");
                            return true;
                        }
                        player.openInventory(new ChestPanel().panel(true));

                        return true;
                    } else if ("craft".equalsIgnoreCase(args[0])) {
                        if (!player.hasPermission("koalarecipe.craft.panel")) {
                            player.sendMessage(ChatColor.RED + "ȱ��Ȩ��<koalarecipe.craft.panel>");
                            return true;
                        }
                        player.openInventory(new ChestPanel().panel(false));

                        return true;
                    } else if ("update".equalsIgnoreCase(args[0])) {
                        if (!player.hasPermission("koalarecipe.update")) {
                            player.sendMessage(ChatColor.RED + "ȱ��Ȩ��<koalarecipe.update>");
                            return true;
                        }
                        new RecipeFile().updateRecipe();
                        player.sendMessage(ChatColor.GOLD + "�䷽�ļ��������");

                        return true;
                    } else if ("debug".equalsIgnoreCase(args[0])) {
                        if (!player.hasPermission("koalarecipe.debug")) {
                            player.sendMessage(ChatColor.RED + "ȱ��Ȩ��<koalarecipe.debug>");
                            return true;
                        }
                        KoalaRecipe.switchDebugMode();
                        player.sendMessage(ChatColor.GOLD + "Debug: " + KoalaRecipe.onDebug);

                        return true;
                    }
                }
            }
        }
        return false;
    }
}
