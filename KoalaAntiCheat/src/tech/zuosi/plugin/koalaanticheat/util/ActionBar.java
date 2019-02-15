package tech.zuosi.plugin.koalaanticheat.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by iwar on 2016/10/5.
 */
public class ActionBar {
    //public KoalaAntiCheat plugin;

    public ActionBar() {
        //this.plugin = KoalaAntiCheat.getINSTANCE();
    }

    public static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
    }

    public static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }

    public static void sendAction(Player p, String msg) {
        /*if(plugin.getConfig().getBoolean(plugin.getDescription().getName() + ".Settings.ColorCodes.Toggle") && colors) {
            msg = theMessages.replaceWithVariables(p, msg);
        }*/
        try {
            Object e;
            Object ppoc;
            Object nmsp;
            Object pcon;
            if(!getServerVersion().equalsIgnoreCase("v1_9_R1") && !getServerVersion().equalsIgnoreCase("v1_9_R2")) {
                if(!getServerVersion().equalsIgnoreCase("v1_8_R2") && !getServerVersion().equalsIgnoreCase("v1_8_R3")) {
                    e = getNmsClass("ChatSerializer").getMethod("a", new Class[]{String.class}).invoke((Object)null, new Object[]{"{\'text\': \'" + msg + "\'}"});
                    ppoc = getNmsClass("PacketPlayOutChat").getConstructor(new Class[]{getNmsClass("IChatBaseComponent"), Byte.TYPE}).newInstance(new Object[]{e, Byte.valueOf((byte)2)});
                    nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
                    pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
                    pcon.getClass().getMethod("sendPacket", new Class[]{getNmsClass("Packet")}).invoke(pcon, new Object[]{ppoc});
                } else {
                    e = getNmsClass("IChatBaseComponent$ChatSerializer").getMethod("a", new Class[]{String.class}).invoke((Object)null, new Object[]{"{\'text\': \'" + msg + "\'}"});
                    ppoc = getNmsClass("PacketPlayOutChat").getConstructor(new Class[]{getNmsClass("IChatBaseComponent"), Byte.TYPE}).newInstance(new Object[]{e, Byte.valueOf((byte)2)});
                    nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
                    pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
                    pcon.getClass().getMethod("sendPacket", new Class[]{getNmsClass("Packet")}).invoke(pcon, new Object[]{ppoc});
                }
            } else {
                e = getNmsClass("ChatComponentText").getConstructor(new Class[]{String.class}).newInstance(new Object[]{ChatColor.translateAlternateColorCodes('&', msg)});
                ppoc = getNmsClass("PacketPlayOutChat").getConstructor(new Class[]{getNmsClass("IChatBaseComponent"), Byte.TYPE}).newInstance(new Object[]{e, Byte.valueOf((byte)2)});
                nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p, new Object[0]);
                pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
                pcon.getClass().getMethod("sendPacket", new Class[]{getNmsClass("Packet")}).invoke(pcon, new Object[]{ppoc});
            }
        } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | NoSuchFieldException | IllegalAccessException var8) {
            var8.printStackTrace();
        }
    }
}
