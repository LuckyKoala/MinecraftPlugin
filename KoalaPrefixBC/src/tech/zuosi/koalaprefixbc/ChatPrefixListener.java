package tech.zuosi.koalaprefixbc;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ConnectedPlayer;
import net.md_5.bungee.api.connection.Connection;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;


/**
 * Created by iwar on 2017/1/25.
 */
public class ChatPrefixListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent event) {
        if(event.isCommand()) return; //Skip command
        //Add prefix here
        Connection connection = event.getSender();
        if(!(connection instanceof ConnectedPlayer)) {
            System.out.println("Not ConnectedPlayer");
            return;
        }
        ConnectedPlayer connectedPlayer = (ConnectedPlayer)connection;
        String prefixStr = DataManager.getPlayerPrefix(connectedPlayer.getName()).getUsingPrefix();
        if(prefixStr.equals("")) return;
        String prefix = ChatColor.translateAlternateColorCodes('&',prefixStr);
        event.setMessage(prefix+'>'+event.getMessage());
    }
}
