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
        //���ö�ʱ����һСʱ��ÿ��һСʱִ��clearMinecart()һ��
        getServer().getScheduler().runTaskTimer(this, this::clearMinecartAfterBroadcast, HOUR_TICKS, HOUR_TICKS);
        logger.fine("Task set.");
    }

    @Override
    public void onDisable() {
        getServer().getScheduler().cancelTasks(this);
        logger.fine("Task cancelled.");
    }

    private void clearMinecartAfterBroadcast() {
        //�㲥��Ϣ
        getServer().broadcastMessage(ChatColor.RED + "���������Ӻ�����δ������ҵĿ�ʵ��~");
        logger.info("Prepare to run clearMinecart()");
        clearMinecart(THREE_MINUTES_TICKS);
    }

    //ע�͵�logger�������Ϊ����������
    private void clearMinecart(long delay) {
        getServer().getScheduler().runTaskLater(this, ()->{
            //logger.entering(getClass().getSimpleName(), "clearMinecart");
            //������������
            for (World world : Bukkit.getWorlds()) {
                //logger.info(String.format("Handling world [%s]", world.getName()));
                //��ȡʵ�弯�ϲ�����
                for (Entity entity : world.getEntitiesByClass(Minecart.class)) {
                    //logger.info(String.format("Handling minecart [%s]", entity.getType()));
                    //ǿ��ת��ΪMinecart��������з���ҳ˿����䵯����֮���Ƴ���
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

    //����/cm��������ִ��һ�������Թ�����
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if ("cm".equalsIgnoreCase(label)) {
            getServer().broadcastMessage(ChatColor.RED + "��ʼ����δ������ҵĿ�ʵ��...");
            clearMinecart(0);
            return true;
        }
        return false;
    }
}
