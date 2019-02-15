package tech.zuosi.minecraft.koalavip.view;

import org.bukkit.Bukkit;
import tech.zuosi.minecraft.koalavip.Core;
import tech.zuosi.minecraft.koalavip.constant.AssetStatus;
import tech.zuosi.minecraft.koalavip.view.template.CommandTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Created by luckykoala on 18-3-20.
 */
public class Command {
    private CommandTemplate template;
    private int id;
    private long lastTimeInvoked;
    private static final Command INVALID_COMMAND = new Command(null, 0L);

    public Command(CommandTemplate template, long lastTimeInvoked) {
        this.id = -1;
        this.template = template;
        this.lastTimeInvoked = lastTimeInvoked;
    }

    public int invokeCmd(String username, long now) {
        if(this==INVALID_COMMAND) return AssetStatus.EXPIRED;
        if(template.isOnetime()) {
            boolean canProcess = lastTimeInvoked==0;
            if(canProcess) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), template.getCmd(username));
                recordUsage(username, now);
                Core.getInstance().debug(() -> "执行单次命令[OK] --> "+ template.getCmd(username) +
                        String.format(" <-- From invokeCmd(%s, %s)", username, now));
                return AssetStatus.OK;
            } else {
                Core.getInstance().debug(() -> "执行单次命令[EXPIRED] --> "+ template.getCmd(username) +
                        String.format(" <-- From invokeCmd(%s, %s)", username, now));
                return AssetStatus.EXPIRED;
            }
        }
        if(lastTimeInvoked == 0) {
            recordUsage(username, now);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), template.getCmd(username));
            Core.getInstance().debug(() -> "首次 执行命令[OK] --> "+ template.getCmd() +
                    String.format(" <-- From invokeCmd(%s, %s)", username, now));
            return AssetStatus.OK;
        } else {
            long timeSpent = TimeUnit.MILLISECONDS.toDays(now-lastTimeInvoked);
            long timeRemain = template.getPeriod() - timeSpent;
            if(timeRemain <= 0) {
                recordUsage(username, now);
                String cmd = template.getCmd(username);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
                Core.getInstance().debug(() -> "执行命令[OK] --> "+cmd+
                        String.format(" <-- From invokeCmd(%s, %s)", username, now));
                return AssetStatus.OK;
            } else {
                Core.getInstance().debug(() -> "执行命令[NOT_TIME] --> "+ template.getCmd(username) +
                        String.format(" <-- From invokeCmd(%s, %s)", username, now));
                return (int)timeRemain;
                //return AssetStatus.NOT_TIME;
            }
        }
    }

    /**
     * Record last time we got reward from cmd and decrease the remainTimes
     */
    private void recordUsage(String username, long now) {
        lastTimeInvoked = now;
        Core.getInstance().getDatabaseManager().getEngine().updateCommand(username, this);
    }

    public void setLastTimeInvoked(long lastTimeInvoked) {
        this.lastTimeInvoked = lastTimeInvoked;
    }

    public CommandTemplate getTemplate() {
        return template;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public long getLastTimeInvoked() {
        return lastTimeInvoked;
    }
}
