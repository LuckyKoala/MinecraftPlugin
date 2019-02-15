package tech.zuosi.koalaattribute.tool;

import org.bukkit.command.CommandSender;

/**
 * Created by iwar on 2017/8/29.
 */
public class Permissions {

    private Permissions() {}

    public static boolean canRemove(CommandSender sender) {
        return sender.hasPermission("koalaattribute.remove");
    }

    public static boolean canAdd(CommandSender sender) {
        return sender.hasPermission("koalaattribute.add");
    }

    public static boolean canGive(CommandSender sender) {
        return sender.hasPermission("koalaattribute.give");
    }

    public static boolean canShowSelf(CommandSender sender) {
        return sender.hasPermission("koalaattribute.show.self");
    }

    public static boolean canShowOther(CommandSender sender) {
        return sender.hasPermission("koalaattribute.show.other");
    }
}
