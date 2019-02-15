package tech.zuosi.rebelwar.util;

import net.minecraft.server.v1_8_R1.EnumTitleAction;
import net.minecraft.server.v1_8_R1.PacketPlayOutTitle;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;

/**
 * Created by iwar on 2016/10/16.
 */
public class TitleUtil {

    public static void sendTitle(Player player,String title, String subtitle) {
        PacketPlayOutTitle packetSubtitle;
        CraftPlayer craftPlayer = (CraftPlayer)player;
        if(title != null) {
            packetSubtitle = new PacketPlayOutTitle(EnumTitleAction.TITLE, CraftChatMessage.fromString(title)[0]);
            craftPlayer.getHandle().playerConnection.sendPacket(packetSubtitle);
        }

        if(subtitle != null) {
            packetSubtitle = new PacketPlayOutTitle(EnumTitleAction.SUBTITLE, CraftChatMessage.fromString(subtitle)[0]);
            craftPlayer.getHandle().playerConnection.sendPacket(packetSubtitle);
        }
    }
}
