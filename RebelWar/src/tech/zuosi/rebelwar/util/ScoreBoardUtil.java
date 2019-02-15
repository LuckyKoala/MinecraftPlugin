package tech.zuosi.rebelwar.util;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import tech.zuosi.rebelwar.RebelWar;
import tech.zuosi.rebelwar.game.manager.QueueManager;
import tech.zuosi.rebelwar.game.object.GamePlayer;

/**
 * Created by iwar on 2016/10/1.
 *
 * 游戏计分板：
 * &eSuperGame                              1
 * &e{所在服务器名字}                        2
 * &e在线: &b{玩家数量}&e/&b{最大玩家数量}    3
 * &e击杀:&b{击杀人数}                       4
 * &e游戏币:&b{游戏币数量}                   5
 * &e倒计时:{倒计时}                         6
 * &ewww.supergames.com                     7
 * 默认倒计时100秒，在剩余30秒，15秒再提示1次，最后10~1显示在玩家屏幕上，为&a{倒计时}会显示在玩家屏幕上.
 * 游戏每间隔5分钟提示一次游戏还剩下多久，游戏提示格式为--&e（语句）&b（变量）【变量和语句空2格】
 */
public class ScoreBoardUtil {
    private static final RebelWar plugin = RebelWar.getINSTANCE();

    public ScoreBoardUtil() {
        //
    }

    private String convert(String str) {
        return ChatColor.translateAlternateColorCodes('&',str);
    }

    public void show(GamePlayer gamePlayer) {
        Scoreboard scoreboard =  Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        Objective scoreObj = scoreboard.registerNewObjective("rebelwar","dummy");
        scoreObj.setDisplaySlot(DisplaySlot.SIDEBAR);
        scoreObj.setDisplayName(convert("&eSuperGame"));

        Score serverName = scoreObj.getScore(convert("&e{serverName}".replace("{serverName}",
                plugin.getServer().getServerName())));
        serverName.setScore(6);
        Score onlineInfo = scoreObj.getScore(convert("&e在线: &b{玩家数量}&e/&b{最大玩家数量}"
                .replace("{玩家数量}",String.valueOf(plugin.getServer().getOnlinePlayers().size()))
                .replace("{最大玩家数量}",String.valueOf(plugin.getServer().getMaxPlayers()))));
        onlineInfo.setScore(5);
        Score killNum = scoreObj.getScore(convert("&e击杀:&b{击杀人数}".replace("{击杀人数}",
                String.valueOf(gamePlayer.getGameStat().getKillCount()))));
        killNum.setScore(4);
        Score cointNum = scoreObj.getScore(convert("&e游戏币:&b{游戏币数量}"
                .replace("{游戏币数量}",String.valueOf(gamePlayer.getGameStat().getCoinCount()))));
        cointNum.setScore(3);

        long reversePassSecond = 0L;
        if (QueueManager.getINSTANCE().isInStartedGame(gamePlayer)) {
            reversePassSecond = gamePlayer.getCurrentGame().getCurrentManager().passSeconds(true);
        }
        String timeInfo = "99:99";
        if (reversePassSecond!=0L) {
            long minute = reversePassSecond/60;
            long second = reversePassSecond%60;
            timeInfo = "m:s".replace("m",String.valueOf(minute)).replace("s",String.valueOf(second));
        }
        Score countDown = scoreObj.getScore(convert("&e倒计时:{倒计时}".replace("{倒计时}",timeInfo)));
        countDown.setScore(2);

        Score urlInfo = scoreObj.getScore(convert("&ewww.supergames.com"));
        urlInfo.setScore(1);
        gamePlayer.getPlayer().setScoreboard(scoreboard);
    }

    public void update(GamePlayer gamePlayer,String oldEntry,String newEntry) {
        Scoreboard currentScoreBoard = gamePlayer.getPlayer().getScoreboard();
        Objective currentObj = currentScoreBoard.getObjective("rebelwar");
        Validate.notNull(currentObj,"无法更新非法的计分板");
        int line = currentObj.getScore(oldEntry).getScore();
        currentScoreBoard.resetScores(oldEntry);
        currentObj.getScore(newEntry).setScore(line);
        gamePlayer.getPlayer().setScoreboard(currentScoreBoard);
    }
}
