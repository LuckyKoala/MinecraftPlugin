package tech.zuosi.rebelwar.game.object;

/**
 * Created by iwar on 2016/9/29.
 */
public class GameConfig {
    private static int MEMBER_LIMIT;
    private static int REBEL_LIMIT;
    private static int TIME_LIMIT; //∑÷÷”
    private static String INFO_PREFIX;
    private static String LOBBY_SERVER_NAME;
    private static GameConfig INSTANCE;

    private GameConfig(boolean selfInit) {
        if (selfInit) {
            MEMBER_LIMIT =16;
            REBEL_LIMIT =1;
            TIME_LIMIT =20;
            INFO_PREFIX ="Default";
            LOBBY_SERVER_NAME="lobby";
        }
    }

    public void initConfig(int memberLimit,int rebelLimit,int timeLimit,String infoPrefix,String lobbyName) {
        MEMBER_LIMIT = memberLimit;
        REBEL_LIMIT = rebelLimit;
        TIME_LIMIT = timeLimit;
        INFO_PREFIX = infoPrefix;
        LOBBY_SERVER_NAME= lobbyName;
        INSTANCE= new GameConfig(false);
    }

    public static GameConfig getInstance() {
        if (INSTANCE == null) {
            INSTANCE=new GameConfig(true);
        }
        return INSTANCE;
    }

    public int getMEMBERLIMIT() {
        return MEMBER_LIMIT;
    }

    public int getREBELLIMIT() {
        return REBEL_LIMIT;
    }

    public int getTIMELIMIT() {
        return TIME_LIMIT;
    }

    public String getINFOPREFIX() {
        return INFO_PREFIX;
    }

    public static String getLobbyServerName() {
        return LOBBY_SERVER_NAME;
    }
}
