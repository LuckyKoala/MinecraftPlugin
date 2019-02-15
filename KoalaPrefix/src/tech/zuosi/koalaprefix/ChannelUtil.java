package tech.zuosi.koalaprefix;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import tech.zuosi.koalaprefix.data.DataManager;
import tech.zuosi.koalaprefix.data.PlayerPrefix;
import tech.zuosi.koalaprefix.data.Prefix;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2017/1/26.
 */
public class ChannelUtil implements PluginMessageListener {
    private static Map<String,String> messageGetCache = new HashMap<>();
    private static ChannelUtil instance;

    private ChannelUtil() {}

    public static ChannelUtil getInstance() {
        if(instance==null) instance=new ChannelUtil();
        return instance;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("Return")) {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(message));
        try {
            String subchannel = in.readUTF();
            if(subchannel.equals("get")){
                String input = in.readUTF();
                PlayerPrefix playerPrefix = PlayerPrefix.loadFromString(player.getName(),input);
                DataManager.cachePlayerPrefix(player.getName(),playerPrefix);
            } else if(subchannel.equals("update")) {
                //update
                DataManager.loadAllPrefix(in.readUTF());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void getPlayerPrefix(Player p,String overrideName) {
        sendToBungeeCord(p, "get", overrideName);
    }

    public void setState(Player p, String prefixName, Prefix.PrefixState state) {
        setState(p,p.getName(),prefixName,state);
    }

    public void setState(Player p,String overrideName, String prefixName, Prefix.PrefixState state) {
        String data = overrideName+':'+prefixName+':'+state.name();
        sendToBungeeCord(p, "set", overrideName+'+'+data);
    }

    public void sendToBungeeCord(Player p, String channel, String sub){
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(b);
        try {
            out.writeUTF(channel);
            out.writeUTF(sub);
        } catch (IOException e) {
            e.printStackTrace();
        }
        p.sendPluginMessage(main.getInstance(), "BungeeCord", b.toByteArray());
    }
}
