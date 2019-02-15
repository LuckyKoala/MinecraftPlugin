package tech.zuosi.clearminecart;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Created by iwar on 2017/7/25.
 */
public class Main extends JavaPlugin {
    private static final long TICKS_PER_SECONDS = 20L;
    private static final long HOUR_TICKS = TimeUnit.HOURS.toSeconds(1)*TICKS_PER_SECONDS;
    private static final long THREE_MINUTES_TICKS = TimeUnit.MINUTES.toSeconds(3)*TICKS_PER_SECONDS;

    private static Logger logger;

    @Override
    public void onEnable() {
        logger = getLogger();
        //设置定时器于一小时后并每隔一小时执行clearMinecart()一次
        getServer().getScheduler().runTaskTimer(this, this::clearMinecartAfterBroadcast, HOUR_TICKS, HOUR_TICKS);
        logger.fine("Task set.");
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        logger.fine("Task cancelled.");
    }

    private void clearMinecartAfterBroadcast() {
        //广播信息
        getServer().broadcastMessage(ChatColor.RED + "将于三分钟后清理未搭载玩家的矿车实体~");
        logger.info("Prepare to run clearMinecart()");
        clearMinecart(THREE_MINUTES_TICKS);
    }

    //注释的logger调用语句为调试输出语句
    private void clearMinecart(long delay) {
        getServer().getScheduler().runTaskLater(this, ()->{
            //logger.entering(getClass().getSimpleName(), "clearMinecart");
            //遍历所有世界
            for (World world : Bukkit.getWorlds()) {
                //logger.info(String.format("Handling world [%s]", world.getName()));
                //获取实体集合并遍历
                for (Entity entity : world.getEntitiesByClass(Minecart.class)) {
                    //logger.info(String.format("Handling minecart [%s]", entity.getType()));
                    //强制转型为Minecart对象，如果有非玩家乘客则将其弹出，之后移除矿车
                    Minecart minecart = (Minecart) entity;
                    if (minecart.isEmpty()) {
                        minecart.remove();
                    } else if (minecart.getPassenger() instanceof Player) {
                        //Do nothing
                    } else {
                        minecart.eject();
                        minecart.remove();
                    }
                }
            }
            //logger.exiting(getClass().getSimpleName(), "clearMinecart");
        }, delay);
    }

    //输入/cm命令立即执行一次清理，以供测试
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ("cm".equalsIgnoreCase(label)) {
            getServer().broadcastMessage(ChatColor.RED + "开始清理未搭载玩家的矿车实体...");
            clearMinecart(0);
            return true;
        }
        return false;
    }
}
