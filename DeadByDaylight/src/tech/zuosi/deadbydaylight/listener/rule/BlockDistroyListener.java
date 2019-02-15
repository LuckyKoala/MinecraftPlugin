package tech.zuosi.deadbydaylight.listener.rule;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

/**
 * Created by iwar on 2016/7/10.
 */
public class BlockDistroyListener implements Listener {


    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockDistroy(BlockBreakEvent bbe) {
        //�ж�����(С��Ϸ�Ļ����ж��Ƿ���Arena��)
        bbe.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onBlockPlace(BlockPlaceEvent bpe) {
        //
        bpe.setCancelled(true);
    }
}
