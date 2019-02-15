package tech.zuosi.loadbalancing;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by iwar on 2016/11/20.
 */
public class Main extends JavaPlugin implements PluginMessageListener,Listener {
    private static Set<String> SERVERSET;
    private static Map<String,LoadGroup> playerPair = new HashMap<>();
    private static Map<String,String> lastServerPair = new HashMap<>();
    private static Set<String> cache = new HashSet<>();
    public static boolean DEBUG = false;

    @Override
    public void onEnable() {
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
        this.getServer().getPluginManager().registerEvents(this,this);
        this.saveDefaultConfig();
        DEBUG = this.getConfig().getBoolean("debug",false);
        SERVERSET = new HashSet<>(this.getConfig().getStringList("Group.server_list"));
        log("onEnable() done...");
    }

    @Override
    public void onDisable() {}

    @EventHandler
    public void onMove(PlayerMoveEvent moveEvent) {
        Player p = moveEvent.getPlayer();
        if(!cache.contains(p.getName())) cache.add(p.getName());
        log(String.format("Player %s Move...", p.getName()));
        queryAllServerStatus(p);
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        log(String.format("PluginMessageReceived...\nChannel: %s", channel));
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("PlayerCount")) {
            String serverName = in.readUTF();
            int playercount = in.readInt();
            String playerName = player.getName();
            if (!playerPair.containsKey(playerName)) {
                playerPair.put(playerName,new LoadGroup(playerName));
            }
            LoadGroup loadGroup = playerPair.get(playerName);
            log(String.format("LoadStat-Message: ServerName[%s],PlayerCount[%s]", serverName,playercount));
            loadGroup.addMember(new LoadStat(serverName,playercount));
            if (lastServerPair.containsKey(playerName) && lastServerPair.get(playerName).equals(serverName)) {
                System.out.println("Debug-Connect...");
                if(cache.contains(player.getName())) {
                    cache.remove(player.getName());
                    connectTo(player, loadGroup.complete());
                }
            }
        }
    }

    private void queryAllServerStatus(Player p) {
        log("queryAllServerStarus...");
        int count = SERVERSET.size();
        for (String server_name:SERVERSET) {
            count--;
            if (count==0) lastServerPair.put(p.getName(),server_name);
            queryPlayerCount(p,server_name);
        }
    }

    private void queryPlayerCount(Player p,String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(serverName);
        p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

    private void connectTo(Player p,String serverName) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("Connect");
        out.writeUTF(serverName);
        p.sendPluginMessage(this, "BungeeCord", out.toByteArray());
    }

    public static void log(String arg) {
        if(!DEBUG) return;
        System.out.println(arg);
    }
}
