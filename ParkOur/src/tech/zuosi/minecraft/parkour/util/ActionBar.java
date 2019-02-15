package tech.zuosi.minecraft.parkour.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by iwar on 2016/5/9.
 */
public class ActionBar {
    private static Class<?> getNmsClass(String nmsClassName) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3] + "." + nmsClassName);
    }
    private static String getServerVersion() {
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
            String version = getServerVersion();
            if(!version.equalsIgnoreCase("v1_9_R1") && !version.equalsIgnoreCase("v1_9_R2")) {
                if(!version.equalsIgnoreCase("v1_8_R2") && !version.equalsIgnoreCase("v1_8_R3")) {
                    e = getNmsClass("ChatSerializer").getMethod("a", new Class[]{String.class}).invoke(null, "{\'text\': \'" + msg + "\'}");
                    ppoc = getNmsClass("PacketPlayOutChat").getConstructor(new Class[]{getNmsClass("IChatBaseComponent"), Byte.TYPE}).newInstance(e, (byte) 2);
                    nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p);
                    pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
                    pcon.getClass().getMethod("sendPacket", new Class[]{getNmsClass("Packet")}).invoke(pcon, ppoc);
                } else {
                    e = getNmsClass("IChatBaseComponent$ChatSerializer").getMethod("a", new Class[]{String.class}).invoke(null, "{\'text\': \'" + msg + "\'}");
                    ppoc = getNmsClass("PacketPlayOutChat").getConstructor(new Class[]{getNmsClass("IChatBaseComponent"), Byte.TYPE}).newInstance(e, (byte) 2);
                    nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p);
                    pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
                    pcon.getClass().getMethod("sendPacket", new Class[]{getNmsClass("Packet")}).invoke(pcon, ppoc);
                }
            } else {
                e = getNmsClass("ChatComponentText").getConstructor(new Class[]{String.class}).newInstance(ChatColor.translateAlternateColorCodes('&', msg));
                ppoc = getNmsClass("PacketPlayOutChat").getConstructor(new Class[]{getNmsClass("IChatBaseComponent"), Byte.TYPE}).newInstance(e, (byte) 2);
                nmsp = p.getClass().getMethod("getHandle", new Class[0]).invoke(p);
                pcon = nmsp.getClass().getField("playerConnection").get(nmsp);
                pcon.getClass().getMethod("sendPacket", new Class[]{getNmsClass("Packet")}).invoke(pcon, ppoc);
            }
        } catch (IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException | ClassNotFoundException | InstantiationException | NoSuchFieldException | IllegalAccessException var8) {
            var8.printStackTrace();
        }
    }
}
