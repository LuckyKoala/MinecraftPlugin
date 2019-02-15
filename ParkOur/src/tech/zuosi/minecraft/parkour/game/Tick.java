package tech.zuosi.minecraft.parkour.game;

import tech.zuosi.minecraft.parkour.Core;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by luckykoala on 18-3-23.
 */
public class Tick {
    private final ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
    public static final long SECONDS_TO_TICK = 20L;

    public void start() {
        service.scheduleAtFixedRate(() -> Core.getInstance().gameManager.tickAll(getCurrentMs()),
                0,1L, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        service.shutdown();
    }

    private long getCurrentMs() {
        return System.currentTimeMillis();
    }
}
