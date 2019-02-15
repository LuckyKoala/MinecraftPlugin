package tech.zuosi.minecraft.parkour.game;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.FireworkMeta;
import tech.zuosi.minecraft.parkour.Core;
import tech.zuosi.minecraft.parkour.database.DataManager;
import tech.zuosi.minecraft.parkour.database.PlayerData;
import tech.zuosi.minecraft.parkour.util.ActionBar;
import tech.zuosi.minecraft.parkour.util.FormatUtil;
import tech.zuosi.minecraft.parkour.util.NBTUtil;
import tech.zuosi.minecraft.parkour.util.tellraw.ClickAction;
import tech.zuosi.minecraft.parkour.util.tellraw.TellRawBuilder;
import tech.zuosi.minecraft.parkour.util.tellraw.TextColor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static tech.zuosi.minecraft.parkour.util.FormatUtil.millisecondsFormat;

/**
 * Created by LuckyKoala on 18-9-18.
 */
@ToString
@EqualsAndHashCode
public class GamePlayer {
    @Getter
    private String playerName;
    @Getter
    private boolean playing; //Indicate whether to handle event
    @Getter
    private boolean moving; //Indicate whether to count time
    private long startTime;
    private static final long NOT_VALID = 0L;
    @Getter
    private Player player;
    private ItemStack[] savedItems;
    @Getter
    private MapPath mapPath;
    private boolean hiding;
    private Set<String> hidedPlayerNames;
    private int taskId;

    GamePlayer(String playerName) {
        this(Bukkit.getPlayerExact(playerName));
    }

    GamePlayer(Player player) {
        this.playerName = player.getName();
        this.playing = false;
        this.moving = false;
        this.hiding = false;
        this.player = player;
        this.hidedPlayerNames = new HashSet<>();
    }

    //====== Run every single tick ======

    void tick(long currentMs) {
        long ms = calculateTimeUsed(currentMs);
        ActionBar.sendAction(player, String.format("Time: %s", millisecondsFormat(ms)));
    }

    //====== Join or leave map ======

    void join(MapPath mapPath) {
        if(this.mapPath != null) this.leave();
        cancelAutoBackTask();
        this.mapPath = mapPath;
        taskId = -1;
        saveAndReplaceInventory();
        toStartPoint();
        joinToMapAndPrepareToStart();
        resetTimer();
        player.setGameMode(GameMode.ADVENTURE);

        GameMap gameMap = Core.getInstance().gameManager.fromMapPath(mapPath);
        PlayerData playerData = Core.getInstance().dataManager.loadPlayerData(playerName);
        List<String> messages = new ArrayList<>();
        messages.add(ChatColor.GRAY + "=====================================");
        messages.add(ChatColor.BLUE + String.format("当前地图为 【%s】", mapPath.toString()));
        messages.add(ChatColor.GRAY + "个人历史最佳时间: "+
                        millisecondsFormat(playerData.bestTime(mapPath)));
        messages.add(ChatColor.GREEN+ "历史评价: "+FormatUtil.stars(playerData.starsUnlocked(mapPath)));
        messages.add(ChatColor.GOLD+ String.format(FormatUtil.STARS_TEMPLATE,
                millisecondsFormat(gameMap.timeRequiredForOneStar),
                millisecondsFormat(gameMap.timeRequiredForTwoStars),
                millisecondsFormat(gameMap.timeRequiredForThreeStars)));
        player.sendMessage(messages.toArray(new String[0]));
    }

    void rejoin(MapPath mapPath) {
        cancelAutoBackTask();
        restoreInventory();
        join(mapPath);
    }

    public void leave() {
        showPlayers();
        cancelAutoBackTask();
        stopOutOfGame();
        resetTimer();
        backToLobby();
        restoreInventory();
        player.setGameMode(GameMode.SURVIVAL);
        player.sendMessage(ChatColor.BLUE + String.format("已离开地图 【%s】", mapPath.toString()));
        this.mapPath = null;
    }

    private void hidePlayers() {
        GameManager gameManager = Core.getInstance().gameManager;
        gameManager.getAllGamePlayer()
                .stream()
                .filter(otherGamePlayer ->
                        mapPath.equals(otherGamePlayer.getMapPath()))
                .map(GamePlayer::getPlayer)
                .forEach(otherPlayer -> {
                    hidedPlayerNames.add(otherPlayer.getName());
                    player.hidePlayer(otherPlayer);
                });
        hiding = true;
    }

    private void showPlayers() {
        if(!hidedPlayerNames.isEmpty()) {
            hidedPlayerNames.forEach(name -> player.hidePlayer(Bukkit.getPlayerExact(name)));
            hidedPlayerNames.clear();
        }
        hiding = false;
    }

    //return value is hiding
    public boolean hideOrShowPlayers() {
        if(hiding) {
            showPlayers();
        } else {
            hidePlayers();
        }
        return hiding;
    }

    private void cancelAutoBackTask() {
        if(taskId!=-1) Bukkit.getScheduler().cancelTask(taskId);
    }

    //====== Game procedure associated with timer ======

    public void startTimer(long currentMs) {
        startTime = currentMs;
    }

    public void resetTimer() {
        startTime = NOT_VALID;
    }

    public void finish() {
        List<String> messages = new ArrayList<>();
        long ms = calculateTimeUsed(System.currentTimeMillis());
        stopOutOfGame();
        resetTimer();

        Location location = player.getLocation();
        player.playSound(location, Sound.LEVEL_UP, 3.0F, 0.5F);

        DataManager dataManager = Core.getInstance().dataManager;
        PlayerData playerData = dataManager.loadPlayerData(playerName);
        //个人最佳?
        long oldBestTime = playerData.bestTime(mapPath);
        boolean newRecord = playerData.newRecord(playerName, mapPath, ms);
        if(newRecord) {
            messages.add(ChatColor.GRAY + "=====================================");
            messages.add(ChatColor.GOLD + "              新的个人最佳时间！");
            messages.add(ChatColor.GRAY + "        之前: "+millisecondsFormat(oldBestTime)
                    + ChatColor.GOLD + "   现在: "+millisecondsFormat(ms));

            Firework fw = location.getWorld().spawn(location.clone().add(0.5, 1, 0.5), Firework.class);
            FireworkMeta fwm = fw.getFireworkMeta();
            FireworkEffect effect = FireworkEffect.builder().withColor(Color.RED).with(FireworkEffect.Type.BALL).build();
            fwm.addEffects(effect);
            fwm.setPower(0);
            fw.setFireworkMeta(fwm);
            fw.detonate();
        }
        //系统最佳
        long oldSystemBestTime = PlayerData.systemBestTimeData.bestTime(mapPath);
        boolean newSystemRecord = PlayerData.systemBestTimeData.newRecord(DataManager.systemBestTimeName, mapPath, ms);
        if(newSystemRecord) {
            messages.add(ChatColor.GRAY + "=====================================");
            messages.add(ChatColor.GOLD + "              新的全服最佳时间！");
            messages.add(ChatColor.GRAY + "        之前: "+millisecondsFormat(oldSystemBestTime)
                    + ChatColor.GOLD + "   现在: "+millisecondsFormat(ms));

            Firework fw = location.getWorld().spawn(location.clone().add(0.5, 1, 0.5), Firework.class);
            FireworkMeta fwm = fw.getFireworkMeta();
            FireworkEffect effect = FireworkEffect.builder().withColor(Color.BLUE).with(FireworkEffect.Type.STAR).build();
            fwm.addEffects(effect);
            fwm.setPower(0);
            fw.setFireworkMeta(fwm);
            fw.detonate();
        }
        GameMap gameMap = Core.getInstance().gameManager.fromMapPath(mapPath);

        messages.add(ChatColor.GRAY + "=====================================");
        messages.add(ChatColor.GREEN+ "你完成了地图 【"+mapPath.toString()+"】,用时 "+millisecondsFormat(ms));
        messages.add(ChatColor.GREEN+ "本次评价 "+FormatUtil.stars(playerData.starsUnlocked(mapPath)));
        messages.add(ChatColor.GOLD+ String.format(FormatUtil.STARS_TEMPLATE,
                millisecondsFormat(gameMap.timeRequiredForOneStar),
                millisecondsFormat(gameMap.timeRequiredForTwoStars),
                millisecondsFormat(gameMap.timeRequiredForThreeStars)));
        messages.add(ChatColor.GRAY + "=====================================");
        messages.add(ChatColor.GOLD + "          感谢游玩！ <10s后自动回到大厅>");
        replaceGameItem();
        player.sendMessage(messages.toArray(new String[0]));

        if(Core.getInstance().gameManager.isPathExist(mapPath.nextLevel())) {
            new TellRawBuilder()
                    .text("        再来一次").color(TextColor.green)
                    .onClick(ClickAction.run_command, "/po option 0").endItem()
                    .text("  下一等级").color(TextColor.dark_purple)
                    .onClick(ClickAction.run_command, "/po option 1").endItem()
                    .text("  返回大厅").color(TextColor.yellow)
                    .onClick(ClickAction.run_command, "/po option 2").endItem()
                    .endAll().send(playerName);
        } else {
            new TellRawBuilder()
                    .text("        再来一次").color(TextColor.green)
                    .onClick(ClickAction.run_command, "/po option 0").endItem()
                    .text("  下一等级(无)").color(TextColor.gray)
                    .onClick(ClickAction.run_command, "/po option 1").endItem()
                    .text("  返回大厅").color(TextColor.yellow)
                    .onClick(ClickAction.run_command, "/po option 2").endItem()
                    .endAll().send(playerName);
        }

        player.sendMessage(ChatColor.GRAY + "=====================================");

        taskId = Bukkit.getScheduler().runTaskLater(Core.getInstance(),
                () -> player.performCommand("po option 2"), 10*Tick.SECONDS_TO_TICK).getTaskId();
    }

    private void replaceGameItem() {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.clear();

        ItemStack replay = new NBTUtil(new ItemStack(Material.REDSTONE))
                .setData("option", 0)
                .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a再来一次&7(右键单击)"))
                .getItemStack();
        ItemStack backToLobby = new NBTUtil(new ItemStack(Material.BARRIER))
                .setData("option", 2)
                .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c返回大厅&7(右键单击)"))
                .getItemStack();
        ItemStack openMenu = new NBTUtil(new ItemStack(Material.PAPER))
                .setData("option", 4)
                .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a打开菜单&7(右键单击)"))
                .getItemStack();

        playerInventory.setHeldItemSlot(3);
        playerInventory.setItemInHand(openMenu);
        playerInventory.setHeldItemSlot(8);
        playerInventory.setItemInHand(backToLobby);

        if(Core.getInstance().gameManager.isPathExist(mapPath.nextLevel())) {
            ItemStack nextLevel = new NBTUtil(new ItemStack(Material.LEVER))
                    .setData("option", 1)
                    .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a下一等级&7(右键单击)"))
                    .getItemStack();
            playerInventory.setHeldItemSlot(1);
            playerInventory.setItemInHand(nextLevel);
        }

        playerInventory.setHeldItemSlot(0);
        playerInventory.setItemInHand(replay);
        player.updateInventory();
    }

    //====== Teleport ======

    public void toStartPoint() {
        GameMap gameMap = Core.getInstance().gameManager.fromMapPath(mapPath);
        player.teleport(gameMap.startPoint);
    }

    private void backToLobby() {
        player.teleport(player.getWorld().getSpawnLocation());
    }

    //====== Inventory handler ======

    private void saveAndReplaceInventory() {
        if(player == null) return;
        PlayerInventory playerInventory = player.getInventory();
        savedItems = playerInventory.getContents();
        playerInventory.clear();

        ItemStack hidePlayers = new NBTUtil(new ItemStack(Material.ENDER_PEARL))
                .setData("option", 3)
                .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&a隐藏玩家&7(右键单击)"))
                .getItemStack();
        ItemStack backToLobby = new NBTUtil(new ItemStack(Material.BARRIER))
                .setData("option", 2)
                .setDisplayName(ChatColor.translateAlternateColorCodes('&', "&c返回大厅&7(右键单击)"))
                .getItemStack();

        playerInventory.setHeldItemSlot(8);
        playerInventory.setItemInHand(backToLobby);
        playerInventory.setHeldItemSlot(1);
        playerInventory.setItemInHand(hidePlayers);
        player.updateInventory();

    }

    private void restoreInventory() {
        if(player == null) return;
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setContents(savedItems);
        playerInventory.setHeldItemSlot(1);
        player.updateInventory();
        savedItems = null;
    }

    //====== Handle moving and playing ======

    private void joinToMapAndPrepareToStart() {
        this.playing = true;
        this.moving = false;
    }

    public void startMoving() {
        this.playing = true;
        this.moving = true;
    }

    public void pauseOnStartPoint() {
        this.playing = true;
        this.moving = false;
    }

    private void stopOutOfGame() {
        this.playing = false;
        this.moving = false;
    }

    //====== Calculation ======

    private long calculateTimeUsed(long currentMs) {
        if(playing && moving && startTime!=NOT_VALID) {
            return currentMs-startTime;
        } else {
            return 0L;
        }
    }
}
