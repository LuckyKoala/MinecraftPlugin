package tech.zuosi.minecraft.koalavip.handler;

import tech.zuosi.minecraft.koalavip.Core;

import java.util.concurrent.TimeUnit;

/**
 * Created by luckykoala on 18-3-23.
 */
public class Tick {
    private static final int MINECRAFT_TICK = 20;
    private long debugOffset = 0L;
    private int taskId;

    public void start() {
        long tickPeriod = Core.getInstance().getConfig().getLong("Period.tick");
        taskId = Core.getInstance().getServer().getScheduler()
                .scheduleSyncRepeatingTask(Core.getInstance(),
                        () -> Core.getInstance().getDatabaseManager().tickAll(getCurrentMs()),
        0L, TimeUnit.MINUTES.toSeconds(tickPeriod)*MINECRAFT_TICK);
    }

    public void stop() {
        Core.getInstance().getServer().getScheduler().cancelTask(taskId);
    }

    public void intercept(long offset) {
        intercept(offset, false);
    }

    public void intercept(long value, boolean reset) {
        if(reset) {
            this.debugOffset = value;
        } else {
            this.debugOffset += value;
        }
        Core.getInstance().debug(() ->
                String.format("reset: %s, debugOffset: %d, value: %d", reset, debugOffset, value));
        Core.getInstance().getDatabaseManager().tickAll(getCurrentMs());
    }

    public long getCurrentMs() {
        long systemMs = System.currentTimeMillis();
        if(Core.getInstance().isDebugOn()) {
            return systemMs + debugOffset;
        } else {
            return systemMs;
        }
    }
}
