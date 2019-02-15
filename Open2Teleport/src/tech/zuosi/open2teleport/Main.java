package tech.zuosi.open2teleport;

import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by iwar on 2016/11/7.
 */
public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        getCommand("ot").setExecutor(this);
        getServer().getPluginManager().registerEvents(this,this);
        ProtocolLibrary.getProtocolManager().addPacketListener(new EPacketListener(this));
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll((Plugin) this);
    }

    @EventHandler
    public void onRegainHealth(EntityRegainHealthEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player p = (Player)e.getEntity();
        Location curLoc = p.getLocation();
        Location tpLoc = getTarLocation();
        if (tpLoc==null) return;
        if (curLoc.distance(tpLoc)<=1D)
            e.setCancelled(true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ot")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED+"��֧�ֿ���̨�������~");
                return false;
            }
            Player player = (Player)sender;
            if (!player.hasPermission("open2teleport.admin")) {
                if (args.length == 1 && "back".equalsIgnoreCase(args[0])) {
                    String name = player.getName();
                    if (Data.wait2back.containsKey(name) && Data.wait2back.get(name)!=null) {
                        Location ori = Data.wait2back.get(name);
                        player.teleport(ori);
                        Data.wait2back.put(name,null);
                        player.sendMessage(ChatColor.GREEN+"�ص�ԭ��.");
                    } else {
                        warn(player,"û����һ�ص�ļ�¼.");
                    }
                    return true;
                } else if (args.length == 0) {
                    info(player,"/ot back ������һ��¼��|���ҽ����ڷ������˳�������ʱ����ʹ��");
                    return true;
                }
                warn(player,"��û��Ȩ��ʹ�øò��������~");
                return true;
            }
            if (args.length == 0) {
                info(player,"�汾:"+getDescription().getVersion());
                info(player,"/ot set ���õ�ǰλ��Ϊ���͵�");
                info(player,"/ot tp ���͵����д��͵�");
                info(player,"/ot back ������һ��¼��");
                return true;
            } else if (args.length == 1) {
                if ("set".equalsIgnoreCase(args[0])) {
                    Location loc = player.getLocation();
                    setTarLocation(loc);
                    info(player,"���͵����óɹ�!");
                    return true;
                } else if ("tp".equalsIgnoreCase(args[0])) {
                    Location dest = getTarLocation();
                    if (dest==null) {
                        warn(player,"δ���ô��͵�,�޷�����!");
                    } else {
                        player.teleport(dest);
                        info(player,"�������!");
                    }
                    return true;
                } else if ("back".equalsIgnoreCase(args[0])) {
                    warn(player,"������Թ���Ա��Ч");
                    return true;
                }
            }
        }

        return false;
    }

    //Util
    private boolean isActive() {
        return getConfig().getBoolean("data.active");
    }

    public Location getTarLocation() {
        return isActive()?(Location)getConfig().get("data.loc"):null;
    }

    private void setTarLocation(Location loc) {
        Location location = getTarLocation();
        if (location == null) {
            getConfig().set("data.active",true);
            getConfig().set("data.loc",loc);
            saveConfig();
        } else if (!location.equals(loc)) {
            getConfig().set("data.loc",loc);
            saveConfig();
        }
    }

    private void echo(Player p,String s) {
        p.sendMessage(Data.PREFIX+s);
    }

    private void info(Player p,String s) {
        echo(p,ChatColor.GREEN+s);
    }

    private void warn(Player p,String s) {
        echo(p,ChatColor.RED+s);
    }
}
