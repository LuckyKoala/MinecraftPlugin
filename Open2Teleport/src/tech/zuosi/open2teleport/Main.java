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
                sender.sendMessage(ChatColor.RED+"不支持控制台命令操作~");
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
                        player.sendMessage(ChatColor.GREEN+"回到原处.");
                    } else {
                        warn(player,"没有上一地点的记录.");
                    }
                    return true;
                } else if (args.length == 0) {
                    info(player,"/ot back 返回上一记录点|当且仅能在非正常退出服务器时可以使用");
                    return true;
                }
                warn(player,"您没有权限使用该插件的命令~");
                return true;
            }
            if (args.length == 0) {
                info(player,"版本:"+getDescription().getVersion());
                info(player,"/ot set 设置当前位置为传送点");
                info(player,"/ot tp 传送到现有传送点");
                info(player,"/ot back 返回上一记录点");
                return true;
            } else if (args.length == 1) {
                if ("set".equalsIgnoreCase(args[0])) {
                    Location loc = player.getLocation();
                    setTarLocation(loc);
                    info(player,"传送点设置成功!");
                    return true;
                } else if ("tp".equalsIgnoreCase(args[0])) {
                    Location dest = getTarLocation();
                    if (dest==null) {
                        warn(player,"未设置传送点,无法传送!");
                    } else {
                        player.teleport(dest);
                        info(player,"传送完毕!");
                    }
                    return true;
                } else if ("back".equalsIgnoreCase(args[0])) {
                    warn(player,"此命令对管理员无效");
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
