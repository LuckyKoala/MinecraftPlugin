package tech.zuosi.minecraft.koalavip.handler;

import org.black_ixx.playerpoints.event.PlayerPointsChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import tech.zuosi.minecraft.koalavip.Core;

import java.util.UUID;

/**
 * Created by luckykoala on 18-3-24.
 */
public class PlayerPointsChangeEventHandler implements Listener {
    @EventHandler
    public void onPointsChange(PlayerPointsChangeEvent event) {
        UUID uuid = event.getPlayerId();
        int change = event.getChange();
        Core.getInstance().debug(() ->
                String.format("onPointsChange() uuid: %s, change: %d", uuid, change));
        if(change < 0) {
            //FIXME 小于0才说明是消费，不过要注意如果是扣钱，这里也算消费了，暂时不管
            Core.getInstance().getDatabaseManager()
                    .get(uuid)
                    .refresh(-change, Core.getInstance().getTick().getCurrentMs());
        }
    }
}
