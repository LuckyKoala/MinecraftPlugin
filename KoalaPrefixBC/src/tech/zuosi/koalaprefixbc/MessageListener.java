package tech.zuosi.koalaprefixbc;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by iwar on 2017/1/25.
 */
public class MessageListener implements Listener {

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        if (e.getTag().equalsIgnoreCase("BungeeCord")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            try {
                String channel = in.readUTF(); // channel we delivered
                if(channel.equals("get")){
                    ServerInfo server = BungeeCord.getInstance().getPlayer(e.getReceiver().toString()).getServer().getInfo();
                    String input = in.readUTF(); // the inputstring
                    PlayerPrefix playerPrefix = DataManager.getPlayerPrefix(input);
                    if(playerPrefix!=null) sendToBukkit("get",playerPrefix.toString(),server);
                } else if(channel.equals("set")) {
                    //ServerInfo server = BungeeCord.getInstance().getPlayer(e.getReceiver().toString()).getServer().getInfo();
                    String[] input = in.readUTF().split(":"); // the inputstring
                    if(input.length!=3) return;
                    String playerName = input[0];
                    String prefixName = input[1];
                    Prefix.PrefixState prefixState = Prefix.PrefixState.valueOf(input[2]);
                    DataManager.setState(playerName,prefixName,prefixState);
                } else if(channel.equals("requireupdate")) {
                    ServerInfo server = BungeeCord.getInstance().getPlayer(e.getReceiver().toString()).getServer().getInfo();
                    List<String> list = new ArrayList<>(1);
                    list.add(server.getName());
                    DataManager.updateAllPrefixToLoadingServer(list);
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }

    public static void sendToBukkit(String channel, String message, ServerInfo server) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(channel);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.sendData("Return", stream.toByteArray());
    }
}
