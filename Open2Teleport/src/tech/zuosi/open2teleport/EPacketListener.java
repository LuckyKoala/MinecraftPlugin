package tech.zuosi.open2teleport;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.concurrent.Callable;


/**
 * Created by iwar on 2016/11/7.
 */
public class EPacketListener extends PacketAdapter {
    Main main;

    public EPacketListener(Main plugin) {
        super(plugin, ListenerPriority.MONITOR, PacketType.Play.Client.CLIENT_COMMAND,PacketType.Play.Client.CLOSE_WINDOW);
        this.plugin = plugin;
        this.main = plugin;
    }

    @Override
    public void onPacketReceiving(PacketEvent pe) {
        Player player = pe.getPlayer();
        String name = player.getName();
        if (pe.getPacketType()==PacketType.Play.Client.CLOSE_WINDOW) {
            PacketContainer container = pe.getPacket();
            int windowID = (Integer) container.getModifier().getValues().get(0);
            if (windowID==0) {
                if (Data.wait2back.containsKey(name) && Data.wait2back.get(name)!=null) {
                    Location ori = Data.wait2back.get(name);
                    Bukkit.getScheduler().callSyncMethod(main, new OneTask(player,ori));
                    Data.wait2back.put(name,null);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            "&8[&a&n系统提示&8] &b背包安全保护关闭"));
                }
            }
        } else if(pe.getPacket().getClientCommands().read(0)
                .toString().equals("OPEN_INVENTORY_ACHIEVEMENT")) {
            if(player.hasPermission("open2teleport.bypass")) return;
            if(Data.simulateSet.contains(name)) {
                Data.simulateSet.remove(name);
                return;
            }
            Location dest = main.getTarLocation();
            if (dest==null) return;
            Data.wait2back.put(name, player.getLocation());
            if(!dest.getWorld().equals(player.getLocation().getWorld())) {
                Data.simulateSet.add(name);
            }
            player.sendMessage(ChatColor.translateAlternateColorCodes('&',
                    "&8[&a&n系统提示&8] &b背包安全保护开启===>关闭背包即可解除保护"));
            Bukkit.getScheduler().callSyncMethod(main, new OneTask(player,dest));
        }
    }

    private class OneTask implements Callable<String> {
        private Player p;
        private Location loc;
        public OneTask(Player p,Location loc) {
            this.p = p;
            this.loc = loc;
        }
        @Override
        public String call() throws Exception {
            p.teleport(loc);
            return "0.0";
        }
    }
}
