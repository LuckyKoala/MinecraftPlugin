package tech.zuosi.minecraft.parkour.handler.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.util.Vector;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.game.GameManager;
import tech.zuosi.minecraft.parkour.game.GameMap;
import tech.zuosi.minecraft.parkour.game.GamePlayer;
import tech.zuosi.minecraft.parkour.util.NBTUtil;

/**
 * Created by LuckyKoala on 18-9-16.
 */
public class InGameListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        GameManager gameManager = Core.getInstance().gameManager;
        Player player = event.getPlayer();
        GamePlayer gamePlayer = gameManager.gamePlayerFor(player);
        if(gamePlayer.isPlaying() && ((Entity)player).isOnGround()) {
            GameMap gameMap = gameManager.fromMapPath(gamePlayer.getMapPath());
            Location to = event.getTo().getBlock().getLocation();

            if(gamePlayer.isMoving()) {
                World toWorld = to.getWorld();
                World gameWorld = gameMap.getStartPoint().getWorld();
                //this.world != other.world && (this.world == null || !this.world.equals(other.world)
                if(toWorld != gameWorld && (toWorld == null || !toWorld.equals(gameWorld))) {
                    //Different world
                    //Case 3 player step out of game region, game reset
                    gamePlayer.toStartPoint();
                    gamePlayer.pauseOnStartPoint();
                    gamePlayer.resetTimer();
                    player.sendMessage(ChatColor.RED + "游戏仍在进行中，请先退出游戏再离开游戏区域");
                } else {
                    //Same world
                    Vector toVector = to.toVector();
                    boolean inRegion = gameMap.getRegions().stream()
                            .anyMatch(region -> region.isInRegion(toVector));
                    if(!inRegion) {
                        //Case 3 player step out of game region, game reset
                        gamePlayer.toStartPoint();
                        gamePlayer.pauseOnStartPoint();
                        gamePlayer.resetTimer();
                    }
                }

            } else {
                if(!to.equals(gameMap.getStartPoint())) {
                    //Case 0 player step out of start point, game start
                    gamePlayer.startMoving();
                    gamePlayer.startTimer(System.currentTimeMillis());
                }
            }
        }
    }

    @EventHandler
    public void onInteractWithGameItem(PlayerInteractEvent event) {
        GameManager gameManager = Core.getInstance().gameManager;
        Player player = event.getPlayer();
        GamePlayer gamePlayer = gameManager.gamePlayerFor(player);
        Action action = event.getAction();

        if(gamePlayer.getMapPath() != null) {
            if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                //Note: 右击空气事件只有手中有物品时才会触发
                ItemStack itemStack = event.getItem();
                if(GUIHandler.isNotAir(itemStack)) {
                    NBTUtil util = new NBTUtil(event.getItem());
                    Integer optionValue = (Integer) util.getData("option");
                    if(optionValue != null) {
                        player.performCommand("po option "+optionValue);
                        event.setCancelled(true);
                        if(Material.ENDER_PEARL == itemStack.getType()) {
                            player.updateInventory();
                        }
                    }
                }
            }
        }
        if(gamePlayer.isPlaying()) {
            if(action == Action.PHYSICAL) {
                Block block = event.getClickedBlock();
                Material material = block.getType();
                if(Material.IRON_PLATE == material || Material.GOLD_PLATE == material) {

                    GameMap gameMap = gameManager.fromMapPath(gamePlayer.getMapPath());
                    if(block.getLocation().equals(gameMap.getStartPoint())) {
                        //Case 1 player back to start point, game reset
                        if(gamePlayer.isMoving()) {
                            gamePlayer.pauseOnStartPoint();
                            gamePlayer.resetTimer();
                        }
                    } else if(block.getLocation().equals(gameMap.getEndPoint())) {
                        //Case 2 player step to end point, game finish
                        gamePlayer.finish();
                    }
                } else {
                    //No other physical interact is allowed
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerPickupItemEvent event) {
        GameManager gameManager = Core.getInstance().gameManager;
        Player player = event.getPlayer();
        GamePlayer gamePlayer = gameManager.gamePlayerFor(player);
        if(gamePlayer.getMapPath() != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        GameManager gameManager = Core.getInstance().gameManager;
        Player player = event.getPlayer();
        GamePlayer gamePlayer = gameManager.gamePlayerFor(player);
        if(gamePlayer.getMapPath() != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if(event.getEntity() instanceof Player) {
            GameManager gameManager = Core.getInstance().gameManager;
            Player player = (Player) event.getEntity();
            GamePlayer gamePlayer = gameManager.gamePlayerFor(player);
            if(gamePlayer.getMapPath() != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            GameManager gameManager = Core.getInstance().gameManager;
            Player player = (Player) event.getEntity();
            GamePlayer gamePlayer = gameManager.gamePlayerFor(player);
            if(gamePlayer.getMapPath() != null) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onDestroy(BlockDamageEvent event) {
        GameManager gameManager = Core.getInstance().gameManager;
        Player player = event.getPlayer();
        GamePlayer gamePlayer = gameManager.gamePlayerFor(player);
        if(gamePlayer.getMapPath() != null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        leaveGameRegionAccidentally(event);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        leaveGameRegionAccidentally(event);
        Core.getInstance().gameManager.removePlayerFromManager(event.getPlayer());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        leaveGameRegionAccidentally(event);
    }

    private void leaveGameRegionAccidentally(PlayerEvent event) {
        GameManager gameManager = Core.getInstance().gameManager;
        Player player = event.getPlayer();
        GamePlayer gamePlayer = gameManager.gamePlayerFor(player);
        if(gamePlayer.getMapPath() != null) {
            gameManager.removePlayerFromGame(player);
        }
    }
}
