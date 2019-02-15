package tech.zuosi.rebelwar.message;

import org.bukkit.ChatColor;
import tech.zuosi.rebelwar.game.object.GamePlayer;
import tech.zuosi.rebelwar.game.object.GameStat;

import java.util.Set;

/**
 * Created by iwar on 2016/9/29.
 */
public class MessageSender {
    private static MessageSender INSTANCE;

    public MessageSender() {}

    public static MessageSender getINSTANCE() {
        if (INSTANCE==null) INSTANCE = new MessageSender();
        return INSTANCE;
    }

    //简单封装使其支持GamePlayer
    public void echo(GamePlayer gamePlayer,String arg) {
        gamePlayer.getPlayer().sendMessage(arg);
    }

    public void echo(GamePlayer gamePlayer,String args[]) {
        gamePlayer.getPlayer().sendMessage(args);
    }

    public void echoAll(Set<GamePlayer> gamePlayerSet,String arg[]) {
        for (GamePlayer gamePlayer:gamePlayerSet) {
            echo(gamePlayer,arg);
        }
    }

    public void echoAll(Set<GamePlayer> gamePlayerSet,String arg) {
        for (GamePlayer gamePlayer:gamePlayerSet) {
            echo(gamePlayer,arg);
        }
    }

    //发送角色分配提示信息
    public void sendRoleInit(Set<GamePlayer> gamePlayerSet) {
        for (GamePlayer gamePlayer:gamePlayerSet) {
            GamePlayer.Status status = gamePlayer.getStatus();
            echo(gamePlayer,ChatColor.GREEN+"角色分配已完毕，您本局的角色为[status]"
                    .replace("status",ChatColor.GOLD+status.getDescription()));
        }
    }

    //广播第一阶段游戏开始提示信息
    public void broadcastGameAStart(Set<GamePlayer> gamePlayerSet) {
        echoAll(gamePlayerSet,new String[]{
                ChatColor.LIGHT_PURPLE + "第一阶段开始~",
                ChatColor.RED + "本阶段目标:",
                ChatColor.GRAY + "反贼: 杀死所有普通玩家/阻止普通玩家放置钥匙",
                ChatColor.GRAY + "普通玩家: 杀死所有反贼/找到并放置一共十把钥匙",
                ChatColor.GOLD + "所有钥匙都被放置后将开启死亡竞赛"
        });
    }

    //广播第二阶段游戏开始提示信息
    public void broadcastGameBStart(Set<GamePlayer> gamePlayerSet) {
        echoAll(gamePlayerSet,new String[]{
                ChatColor.LIGHT_PURPLE + "第二阶段开始~",
                ChatColor.RED + "本阶段目标:",
                ChatColor.GRAY + "反贼: 杀死所有普通玩家",
                ChatColor.GRAY + "普通玩家: 杀死所有反贼",
                ChatColor.GOLD + "任意一方阵营人数减少为0时游戏结束"
        });
    }

    //游戏倒计时提示 mode 
    public void broadcastGameTime(Set<GamePlayer> gamePlayerSet,int mode,String... arg) {
        //
    }

    //游戏结束后发送数据统计信息
    public void sendCurrentStat(GamePlayer gamePlayer) {
        /*
        * &2===============================================================

	      &f&l击杀:{击杀数量} （击杀1获得3游戏币）
	      &f&l参与游戏，获得1游戏币
	      &f&l获得:{游戏币} 游戏币

	      &2===============================================================
        */
        GameStat gameStat = gamePlayer.getGameStat();
        echo(gamePlayer,new String[]{
                ChatColor.translateAlternateColorCodes('&',
                        "&2==============================================================="),
                "\n",
                ChatColor.translateAlternateColorCodes('&',
                        "&f&l击杀:{击杀数量} (击杀1获得3游戏币)"
                                .replace("{击杀数量}",String.valueOf(gameStat.getKillCount()))),
                ChatColor.translateAlternateColorCodes('&',
                        "&f&l参与游戏，获得1游戏币"),
                ChatColor.translateAlternateColorCodes('&',
                        "&f&l获得:{游戏币} 游戏币"
                                .replace("{击杀数量}",String.valueOf(gameStat.getCoinCount()))),
                "\n",
                ChatColor.translateAlternateColorCodes('&',
                        "&2===============================================================")
        });
    }

    //玩家获得钥匙
    public void broadcastKeyPlace(Set<GamePlayer> gamePlayerSet) {
        echoAll(gamePlayerSet,ChatColor.GREEN+"一把钥匙已经成功放置");
    }
}
