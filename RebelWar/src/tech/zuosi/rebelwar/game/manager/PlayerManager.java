package tech.zuosi.rebelwar.game.manager;

import org.bukkit.Bukkit;
import tech.zuosi.rebelwar.game.object.GamePlayer;
import tech.zuosi.rebelwar.util.ScoreBoardUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/9/30.
 * |-PlayerManager 玩家管理
 |-HashMap<String,GamePlayer> 通过玩家ID获得GamePlayer实例
 |-getGamePlayer(String) 若没有则构造一个新的对象
 |-addGamePlayer(String,GamePlayer)
 */
public class PlayerManager {
    private static Map<String,GamePlayer> gamePlayerMap = new HashMap<>();
    private static PlayerManager INSTANCE;

    private PlayerManager() {}

    public GamePlayer getGamePlayer(String playerName) {
        if (gamePlayerMap.containsKey(playerName)) {
            return gamePlayerMap.get(playerName);
        }
        addGamePlayer(playerName);
        return gamePlayerMap.get(playerName);
    }

    public boolean addGamePlayer(String playerName) {
        if (gamePlayerMap.containsKey(playerName)) {
            return false;
        }
        GamePlayer gamePlayer = new GamePlayer(playerName);
        return addGamePlayer(playerName,gamePlayer,true);
    }

    public boolean addGamePlayer(String playerName,GamePlayer gamePlayer,boolean override) {
        if (!override && gamePlayerMap.containsKey(playerName)) {
            return false;
        }
        gamePlayerMap.put(playerName,gamePlayer);
        new ScoreBoardUtil().show(gamePlayer);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),"gamemode 2 "+gamePlayer.getPlayerName());
        return true;
    }

    public static Map<String, GamePlayer> getGamePlayerMap() {
        return gamePlayerMap;
    }

    public static void setGamePlayerMap(Map<String, GamePlayer> gamePlayerMap) {
        PlayerManager.gamePlayerMap = gamePlayerMap;
    }

    public static PlayerManager getInstance() {
        if (INSTANCE==null) INSTANCE=new PlayerManager();
        return INSTANCE;
    }
}
