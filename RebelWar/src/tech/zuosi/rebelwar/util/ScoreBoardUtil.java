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
 * ��Ϸ�Ʒְ壺
 * &eSuperGame                              1
 * &e{���ڷ���������}                        2
 * &e����: &b{�������}&e/&b{����������}    3
 * &e��ɱ:&b{��ɱ����}                       4
 * &e��Ϸ��:&b{��Ϸ������}                   5
 * &e����ʱ:{����ʱ}                         6
 * &ewww.supergames.com                     7
 * Ĭ�ϵ���ʱ100�룬��ʣ��30�룬15������ʾ1�Σ����10~1��ʾ�������Ļ�ϣ�Ϊ&a{����ʱ}����ʾ�������Ļ��.
 * ��Ϸÿ���5������ʾһ����Ϸ��ʣ�¶�ã���Ϸ��ʾ��ʽΪ--&e����䣩&b��������������������2��
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
        Score onlineInfo = scoreObj.getScore(convert("&e����: &b{�������}&e/&b{����������}"
                .replace("{�������}",String.valueOf(plugin.getServer().getOnlinePlayers().size()))
                .replace("{����������}",String.valueOf(plugin.getServer().getMaxPlayers()))));
        onlineInfo.setScore(5);
        Score killNum = scoreObj.getScore(convert("&e��ɱ:&b{��ɱ����}".replace("{��ɱ����}",
                String.valueOf(gamePlayer.getGameStat().getKillCount()))));
        killNum.setScore(4);
        Score cointNum = scoreObj.getScore(convert("&e��Ϸ��:&b{��Ϸ������}"
                .replace("{��Ϸ������}",String.valueOf(gamePlayer.getGameStat().getCoinCount()))));
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
        Score countDown = scoreObj.getScore(convert("&e����ʱ:{����ʱ}".replace("{����ʱ}",timeInfo)));
        countDown.setScore(2);

        Score urlInfo = scoreObj.getScore(convert("&ewww.supergames.com"));
        urlInfo.setScore(1);
        gamePlayer.getPlayer().setScoreboard(scoreboard);
    }

    public void update(GamePlayer gamePlayer,String oldEntry,String newEntry) {
        Scoreboard currentScoreBoard = gamePlayer.getPlayer().getScoreboard();
        Objective currentObj = currentScoreBoard.getObjective("rebelwar");
        Validate.notNull(currentObj,"�޷����·Ƿ��ļƷְ�");
        int line = currentObj.getScore(oldEntry).getScore();
        currentScoreBoard.resetScores(oldEntry);
        currentObj.getScore(newEntry).setScore(line);
        gamePlayer.getPlayer().setScoreboard(currentScoreBoard);
    }
}
