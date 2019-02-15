package tech.zuosi.rebelwar.game.object;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import tech.zuosi.rebelwar.game.manager.QueueManager;

import java.util.*;

/**
 * Created by iwar on 2016/9/29.
 */

@SerializableAs("GamePlayer")
public class GamePlayer implements ConfigurationSerializable {
    private Player player;
    private String playerName;
    private GameStat gameStat;
    private Status status = Status.NONE;
    private Set<Profession> professions = new HashSet<>();
    private Profession defaultProfession = Profession.NORMAL;
    private List<ItemStack> nextItems = new ArrayList<>();

    private Game currentGame;

    /**
     * 序列化时不会直接保存Player对象，而是存储Name
     * 反序列化后新的对象中Player对象为NULL，因此使用GamePlayer对象时必须调用load()方法
     */
    public GamePlayer(String playerName) {
        this.playerName = playerName;
        this.gameStat = new GameStat();
    }

    public GamePlayer(Player player) {
        this.player = player;
        this.playerName = player.getName();
        this.gameStat = new GameStat();
    }

    public GamePlayer(Player player,GameStat gameStat) {
        this.player = player;
        this.playerName = player.getName();
        this.gameStat = gameStat;
    }

    public enum Status {
        NONE("等待"),
        DEAD("死亡"),
        REBEL("反贼"),
        PLAYER("玩家");

        private String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }
    }

    public enum Profession {
        NORMAL("平民"),
        INFANTRYMAN("步兵"),
        SCOUT("侦察兵"),
        TANK("坦克"),
        SOLDIER("战士"),
        LENGEND("战神");


        private String description;

        Profession(String description) {
            this.description = description;
        }

        public String getDescription() {
            return this.description;
        }

        public static Profession getProById(int id) {
            id++; //Skip NORMAL
            for (Profession pro:Profession.values()) {
                if (pro.ordinal() == id) {
                    return pro;
                }
            }
            return null;
        }
    }

    public void bindGame(Game game) {
        this.currentGame = game;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public GameStat getGameStat() {
        return gameStat;
    }

    public void setGameStat(GameStat gameStat) {
        this.gameStat = gameStat;
    }

    public Player getPlayer() {
        load();
        return player;
    }

    public Set<Profession> getProfessions() {
        return professions;
    }

    public boolean addProfession(Profession profession) {
        return this.professions.add(profession);
    }

    public Profession getDefaultProfession() {
        return defaultProfession;
    }

    public void setDefaultProfession(Profession defaultProfession) {
        this.defaultProfession = defaultProfession;
    }

    public void clearNextItems() {this.nextItems.clear();}

    public void addToNextItems(ItemStack itemStack) {
        this.nextItems.add(itemStack);
    }

    public String getPlayerName() {
        return playerName;
    }

    public Game getCurrentGame() {
        return QueueManager.getINSTANCE().isInStartedGame(this)?currentGame:null;
    }

    private GamePlayer load() {
        if (this.player==null) {
            this.player=Bukkit.getPlayer(this.playerName);
        }
        return this;
    }

    @Override
    public boolean equals(Object tar) {
        if (this == tar) return true;
        if (tar == null) return false;
        if (getClass() != tar.getClass()) return false;
        GamePlayer tarGP = (GamePlayer) tar;
        return playerName.equals(tarGP.playerName);
    }

    @Override
    public int hashCode() {
        return playerName.hashCode();
    }

    @Override
    public Map<String,Object> serialize() {
        Set<String> proStr = new HashSet<>();
        for (Profession pro:professions) {
            proStr.add(pro.name());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("playerName",playerName);
        map.put("gameStat",gameStat);
        map.put("status",status.name());
        map.put("professions",proStr);
        map.put("defaultProfession",defaultProfession.name());
        map.put("nextItems",nextItems);
        return map;
    }

    @SuppressWarnings("unchecked")
    public static GamePlayer deserialize(Map<String,Object> map) {
        GamePlayer gamePlayer = new GamePlayer((String) map.get("playerName"));
        Set<Profession> proSet = new HashSet<>();
        Set<String> proStr = (Set<String>) map.get("professions");
        for (String str:proStr) {
            proSet.add(Profession.valueOf(str));
        }
        gamePlayer.setGameStat((GameStat) map.get("gameStat"));
        gamePlayer.setStatus(Status.valueOf((String) map.get("status")));
        gamePlayer.professions = proSet;
        gamePlayer.setDefaultProfession(Profession.valueOf((String) map.get("defaultProfession")));
        gamePlayer.nextItems = (List<ItemStack>) map.get("nextItems");
        return gamePlayer;
    }
}
