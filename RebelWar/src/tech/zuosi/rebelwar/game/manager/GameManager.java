package tech.zuosi.rebelwar.game.manager;

import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import tech.zuosi.rebelwar.RebelWar;
import tech.zuosi.rebelwar.game.object.*;
import tech.zuosi.rebelwar.message.MessageSender;
import tech.zuosi.rebelwar.util.LocationUtil;

import java.util.*;

/**
 * Created by iwar on 2016/9/29.
 * |-GameManager 游戏管理
 |-GameManager(Game) 传入Game实例构造GameManger对象
 |-check() 检测地图完整性
 |-show() 初始化场地，分配玩家身份，发放初始装备，选定十个箱子放置钥匙
 |-startA() 开始游戏，关联并启用相关监听器
 |-startB() 开始第二阶段，关联并启用相关监听器
 |-end() 游戏结束，广播结束信息
 |-timer 定时器
 |-echo() 将玩家与场地关联，通知队列管理类
 */
public class GameManager {
    private Game game;
    private Set<GamePlayer> gamePlayerSet;
    private Set<Location> chestLocationSet;

    private int timeCheckerId;
    private int countDownId;
    private static final long minuteGameTicks = 1200L;
    private long startTimeStamp;

    private int keyFound;
    private int stageNum; //1A 2B
    private int rebelNum;
    private int playerNum;

    public GameManager(Game game) {
        this.game = game;
        this.game.startGame();
        this.game.bindManager(this);
        QueueManager.getINSTANCE().updateState(game.getArena().getName(),false);
        this.gamePlayerSet = this.game.getGamePlayerSet();
        this.chestLocationSet = this.game.getArena().getChestSet();
        this.keyFound = 0;
        init();
    }

    public int getKeyNum() {
        return this.keyFound;
    }

    public int addKey() {
        if (++this.keyFound == 10) startB();
        return this.keyFound;
    }

    public int delRebel() {
        if (--this.rebelNum == 0) end();
        return this.rebelNum;
    }

    public int delPlayer() {
        if (--this.playerNum == 0) end();
        return this.playerNum;
    }

    public void quit(GamePlayer gamePlayer) {
        if (GamePlayer.Status.PLAYER == gamePlayer.getStatus()) {
            gamePlayer.getCurrentGame().getCurrentManager().delPlayer();
        } else if (GamePlayer.Status.REBEL == gamePlayer.getStatus()) {
            gamePlayer.getCurrentGame().getCurrentManager().delRebel();
        }
        endStat(gamePlayer,true);
    }

    private void init() {
        initChest();
        tpA();
        initRole();
        initEquipment();
        startA();
    }

    private void end() {
        stopTimer();
        removeEquipment();
        updateRole();
        updateChest();
        //发送通知信息
        endStat();
        //重置游戏局和队列状态
        Arena arena = this.game.getArena();
        QueueManager.getINSTANCE().removeFromQueue(arena.getName());
        QueueManager.getINSTANCE().addToQueue(arena.getName(),arena);
    }


    //中途退出时quitMode=true
    public void endStat(GamePlayer gamePlayer,boolean quitMode) {
        //特殊退出
        Location loc = gamePlayer.getPlayer().getWorld().getSpawnLocation();

        if (quitMode) {
            this.gamePlayerSet.remove(gamePlayer);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"gamemode 2 "+gamePlayer.getPlayerName());
        }
        if (GamePlayer.Profession.SCOUT == gamePlayer.getDefaultProfession()
                && gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.SPEED)) {
            gamePlayer.getPlayer().removePotionEffect(PotionEffectType.SPEED);
        }
        //可能产生后续空指针
        gamePlayer.bindGame(null);
        gamePlayer.getPlayer().setBedSpawnLocation(loc,true);
        gamePlayer.getPlayer().teleport(loc);
        MessageSender.getINSTANCE().sendCurrentStat(gamePlayer);
    }

    private void endStat() {
        for (GamePlayer gamePlayer:gamePlayerSet) {
            endStat(gamePlayer,false);
        }
        setMode();
    }

    private void setMode() {
        for (GamePlayer gamePlayer:gamePlayerSet) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"gamemode 2 "+gamePlayer.getPlayerName());
        }
    }

    public boolean boundCheck(GamePlayer gamePlayer) {
        Arena arena = game.getArena();
        final LocationUtil util1 = new LocationUtil(arena.getMainLocationDown(),arena.getMainLocationUp());
        final LocationUtil util2 = new LocationUtil(arena.getSubLocationDown(),arena.getSubLocationUp());

        Location loc = gamePlayer.getPlayer().getLocation();
        if (stageNum==1) {
            return util1.isInBound(loc);
        } else if (stageNum==2) {
            return util2.isInBound(loc);
        }
        return false;
    }

    private void initChest() {
        if (chestLocationSet.size() == 10) return;
        Random random = new Random();
        int count = chestLocationSet.size()-10;
        Set<Location> vanishChestLocationSet = new HashSet<>();
        for (Location loc:chestLocationSet) {
            if (count>0) {
                if (random.nextInt(2)==1) vanishChestLocationSet.add(loc);
                count--;
                continue;
            }
            break;
        }
        chestLocationSet.removeAll(vanishChestLocationSet);
        for (Location loc:chestLocationSet) {
            loc.getBlock().setType(Material.CHEST);
        }
        for (Location loc:vanishChestLocationSet) {
            loc.getBlock().setType(Material.AIR);
        }
    }

    //reverse 显示倒计时时间
    public long passSeconds(boolean reverse) {
        long seconds = (System.currentTimeMillis()-startTimeStamp)*1000;
        return reverse? (long) GameConfig.getInstance().getTIMELIMIT()*60-seconds:seconds;
    }

    private void tpA() {
        Location loc = this.game.getArena().getMainTp().getBlock().getRelative(BlockFace.UP).getLocation();
        for (GamePlayer gamePlayer:this.gamePlayerSet) {
            gamePlayer.getPlayer().teleport(loc);
            gamePlayer.getPlayer().setBedSpawnLocation(loc,true);
        }
        setMode();
    }

    private void tpB() {
        Location loc = this.game.getArena().getSubTp().getBlock().getRelative(BlockFace.UP).getLocation();
        for (GamePlayer gamePlayer:this.gamePlayerSet) {
            gamePlayer.getPlayer().teleport(loc);
            gamePlayer.getPlayer().setBedSpawnLocation(loc,true);
        }
    }

    private void initRole() {
        int rebelLimit = GameConfig.getInstance().getREBELLIMIT();
        Random random = new Random();
        List<GamePlayer> gamePlayerList = new ArrayList<>();

        gamePlayerList.addAll(gamePlayerSet);
        for (int count = rebelLimit;count > 0;count--) {
            GamePlayer gamePlayer = gamePlayerList.remove(random.nextInt(gamePlayerList.size()));
            gamePlayer.setStatus(GamePlayer.Status.REBEL);
        }
        for (GamePlayer gamePlayer:gamePlayerList) {
            gamePlayer.setStatus(GamePlayer.Status.PLAYER);
        }
        this.rebelNum = rebelLimit;
        this.playerNum = gamePlayerList.size();

        MessageSender.getINSTANCE().sendRoleInit(gamePlayerSet);
    }

    private void initEquipment() {
        ItemStack stoneSword = new ItemStack(Material.STONE_SWORD);
        ItemStack ironSword = new ItemStack(Material.IRON_SWORD);
        ItemStack leather[] = {new ItemStack(Material.LEATHER_HELMET),new ItemStack(Material.LEATHER_CHESTPLATE),new ItemStack(Material.LEATHER_LEGGINGS),new ItemStack(Material.LEATHER_BOOTS)};
        List<ItemStack> tmp = Arrays.asList(leather);
        ItemStack white[] = new ItemStack[tmp.size()+1];
        ItemStack black[] = new ItemStack[tmp.size()+1];

        //MDZZ
        for (ItemStack leatherIS:tmp) {
            LeatherArmorMeta meta = (LeatherArmorMeta) leatherIS.getItemMeta();
            meta.setColor(Color.BLACK);
            leatherIS.setItemMeta(meta);
        }
        tmp.add(stoneSword);
        black = tmp.toArray(black);
        for (ItemStack _leatherIS:tmp) {
            ItemStack leatherIS = _leatherIS.clone();
            LeatherArmorMeta meta = (LeatherArmorMeta) leatherIS.getItemMeta();
            meta.setColor(Color.WHITE);
            leatherIS.setItemMeta(meta);
            tmp.remove(_leatherIS);
            tmp.add(leatherIS);
        }
        tmp.remove(stoneSword);
        tmp.add(ironSword);
        white = tmp.toArray(white);


        //判断默认职业
        for (GamePlayer gamePlayer:gamePlayerSet) {
            GamePlayer.Profession defaultPro = gamePlayer.getDefaultProfession();
            if (GamePlayer.Profession.NORMAL == defaultPro) {
                if (GamePlayer.Status.PLAYER == gamePlayer.getStatus()) {
                    gamePlayer.getPlayer().getInventory().addItem(white);
                } else if (GamePlayer.Status.REBEL == gamePlayer.getStatus()) {
                    gamePlayer.getPlayer().getInventory().addItem(black);
                }
            } else {
                gamePlayer.getPlayer().getInventory().addItem(GameShop.getInstance().getProfessionItems(defaultPro));
                //侦察兵给予速度2
                if (GamePlayer.Profession.SCOUT == defaultPro) {
                    gamePlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED,
                            (int)(GameConfig.getInstance().getTIMELIMIT()*minuteGameTicks),1));
                }
            }
        }
    }

    private void startA() {
        stageNum = 1;
        this.startTimeStamp = System.currentTimeMillis();
        startTimer();
        for (GamePlayer gamePlayer:gamePlayerSet) {
            if (GamePlayer.Status.REBEL == gamePlayer.getStatus()) {
                gamePlayer.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                        (int)(GameConfig.getInstance().getTIMELIMIT()* minuteGameTicks),0));
            }
        }
        MessageSender.getINSTANCE().broadcastGameAStart(this.gamePlayerSet);
    }

    private void startB() {
        stageNum = 2;
        tpB();
        for (GamePlayer gamePlayer:gamePlayerSet) {
            Player player = gamePlayer.getPlayer();
            if (GamePlayer.Status.REBEL == gamePlayer.getStatus()) {
                if (gamePlayer.getPlayer().hasPotionEffect(PotionEffectType.INVISIBILITY)) {
                    gamePlayer.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
                }
            }
            player.setNoDamageTicks(600);
            player.setHealth(player.getMaxHealth());
            player.setExhaustion(20.0F);
        }
        MessageSender.getINSTANCE().broadcastGameBStart(this.gamePlayerSet);
        //所有玩家将传送到B场地开始死亡竞赛，发放初始装备
        //(反贼为黑色皮革套，保护3，武器为锋利I钻石剑。玩家为白色皮革套，保护1武器为锋利II铁剑)
    }

    private void startTimer() {
        this.timeCheckerId = Bukkit.getScheduler().scheduleSyncDelayedTask(RebelWar.getINSTANCE(), new Runnable() {
            @Override
            public void run() {
                end();
            }
        },GameConfig.getInstance().getTIMELIMIT()* minuteGameTicks);
        this.countDownId = Bukkit.getScheduler().scheduleSyncRepeatingTask(RebelWar.getINSTANCE(), new Runnable() {
            @Override
            public void run() {
                MessageSender.getINSTANCE().echoAll(gamePlayerSet,
                        ChatColor.BLUE + "{prefix}{min}分钟"
                                .replace("{prefix}",GameConfig.getInstance().getINFOPREFIX())
                                .replace("{min}",String.valueOf(passSeconds(true)/60L)));
            }
        },5* minuteGameTicks,0L);
    }

    private void removeEquipment() {
        for (GamePlayer gamePlayer:gamePlayerSet) {
            gamePlayer.getPlayer().getInventory().clear();
        }
    }

    private void updateChest() {
        for (Location loc:this.game.getArena().getChestSet()) {
            loc.getBlock().setType(Material.CHEST);
        }
    }

    private void updateRole() {
        for (GamePlayer gamePlayer:this.gamePlayerSet) {
            gamePlayer.setStatus(GamePlayer.Status.NONE);
        }
    }

    private void stopTimer() {
        Bukkit.getScheduler().cancelTask(this.timeCheckerId);
        Bukkit.getScheduler().cancelTask(this.countDownId);
    }


}
