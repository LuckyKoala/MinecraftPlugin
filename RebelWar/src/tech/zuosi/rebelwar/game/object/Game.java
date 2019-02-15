package tech.zuosi.rebelwar.game.object;

import tech.zuosi.rebelwar.game.manager.GameManager;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by iwar on 2016/9/29.
 */
public class Game {
    private boolean gameStart;
    private boolean gameReady;
    private Arena arena;
    private Set<GamePlayer> gamePlayerSet = new HashSet<>();

    private GameManager currentManager;

    public Game(Arena arena) {
        this.arena = arena;
        this.gameStart = false;
        this.gameReady = false;
    }

    public void bindManager(GameManager gameManager) {
        this.currentManager = gameManager;
    }

    public boolean playerJoin(GamePlayer gamePlayer) {
        if (!gameStart && gamePlayerSet.size()< GameConfig.getInstance().getMEMBERLIMIT()) {
            gamePlayerSet.add(gamePlayer);
            updateReadyStatus();
            return true;
        }
        return false;
    }

    // -1 该游戏中不包含指定玩家
    // 0 游戏已经开始，请输入/rebel quit退出游戏
    // 1 成功退出队列
    public int playerLeave(GamePlayer gamePlayer) {
        if (gamePlayerSet.contains(gamePlayer)) {
            if (gameStart) return 0;
            gamePlayerSet.remove(gamePlayer);
            updateReadyStatus();
            return 1;
        }
        return -1;
    }

    public boolean startGame() {
        if (gameStart) return false;
        gameStart=true;
        return true;
    }

    public boolean isReady() {
        updateReadyStatus();
        return gameReady;
    }

    public boolean hasStart() {
        return gameStart;
    }

    public boolean isInGame(GamePlayer gamePlayer) {
        return gamePlayerSet.contains(gamePlayer);
    }

    private void updateReadyStatus() {
        if (gameReady && gamePlayerSet.size()<GameConfig.getInstance().getMEMBERLIMIT()) {
            this.gameReady = false;
        } else if (!gameReady && gamePlayerSet.size()==GameConfig.getInstance().getMEMBERLIMIT()) {
            this.gameReady=true;
        }
    }

    public Arena getArena() {
        return arena;
    }

    public Set<GamePlayer> getGamePlayerSet() {
        return gamePlayerSet;
    }

    public GameManager getCurrentManager() {
        return currentManager;
    }
}
