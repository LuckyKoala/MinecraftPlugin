package tech.zuosi.rebelwar.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import tech.zuosi.rebelwar.RebelWar;

/**
 * Created by iwar on 2016/10/3.
 */
public class BCChannel {
    private static void sendBungeeCommand(Player p, String... msg) {
        ByteArrayDataOutput out	= ByteStreams.newDataOutput();
        for( String s : msg) out.writeUTF(s);
        p.sendPluginMessage(RebelWar.getINSTANCE(), "BungeeCord", out.toByteArray());
    }

    public static void teleportPlayerTo(Player p, String server) {
        sendBungeeCommand(p, "Connect", server);
    }
}
