package tech.zuosi.deadbydaylight.util;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import tech.zuosi.deadbydaylight.DeadByDaylight;

/**
 * Created by iwar on 2016/7/9.
 */
public class MediaUtil {
    //TODO 显示玩家轮廓，足迹，血迹等
    private DeadByDaylight plugin;

    public MediaUtil(DeadByDaylight plugin) {
        this.plugin = plugin;
    }

    public void showEntity(LivingEntity entity) {
        entity.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,40,0));
    }

    public void showStructure(Location location) {
        Location spawnLocation = location.getBlock().getRelative(BlockFace.NORTH,2).getLocation();
        Sheep sheep = (Sheep) spawnLocation.getWorld().spawnEntity(spawnLocation,EntityType.SHEEP);
        showEntity(sheep);
        new BukkitRunnable() {
            @Override
            public void run() {
                sheep.remove();
            }
        }.runTaskLater(plugin,40);
    }

    public static void showFootstep(Player player,int amount) {
        player.spawnParticle(Particle.FOOTSTEP,player.getLocation(),amount);
    }

    public void showBloodstain(Player player) {
        player.getWorld().playEffect(player.getLocation(), Effect.HEART, 1, 5);
    }

    public void playGasp(Player player) {
        //喘息声
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_BREATH,1.0F,1.0F);
    }

    public void playHeartbeat(Player player) {
        //心跳声
        //TODO 记得游戏结束时 stopSound
        player.playSound(player.getLocation(),Sound.BLOCK_WOOD_PLACE,1.0F,1.0F);
    }
}
