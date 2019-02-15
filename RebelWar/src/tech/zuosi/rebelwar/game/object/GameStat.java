package tech.zuosi.rebelwar.game.object;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by iwar on 2016/9/29.
 */

@SerializableAs("GameStat")
public class GameStat implements ConfigurationSerializable {
    private int coinCount;  //游戏币数量
    private int killCount;  //历史击杀数

    private int currentKillCount;

    public GameStat() {
        this.coinCount = 0;
        this.killCount = 0;
    }

    public GameStat(int coinCount,int killCount) {
        this.coinCount = coinCount;
        this.killCount = killCount;
    }

    public int addCoin(int count) {
        this.coinCount+=count;
        return this.coinCount;
    }

    public int delCoin(int count) {
        this.coinCount-=count;
        return this.coinCount;
    }

    public int addKill() {
        return addKill(1);
    }

    public int addKill(int count) {
        this.killCount+=count;
        this.currentKillCount+=count;
        return this.killCount;
    }

    public void resetCurrentKillCount() {this.currentKillCount=0;}

    public int getCoinCount() {
        return coinCount;
    }

    public void setCoinCount(int coinCount) {
        this.coinCount = coinCount;
    }

    public int getKillCount() {
        return killCount;
    }

    public void setKillCount(int killCount) {
        this.killCount = killCount;
    }

    @Override
    public Map<String,Object> serialize() {
        Map<String,Object> map = new HashMap<>();
        map.put("coinCount",coinCount);
        map.put("killCount",killCount);
        return map;
    }

    public static GameStat deserialize(Map<String,Object> map) {
        GameStat gameStat = new GameStat();
        gameStat.setCoinCount((Integer) map.get("coinCount"));
        gameStat.setKillCount((Integer) map.get("killCount"));
        return gameStat;
    }
}
