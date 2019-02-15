package tech.zuosi.rebelwar.game.manager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import tech.zuosi.rebelwar.RebelWar;
import tech.zuosi.rebelwar.game.object.Arena;
import tech.zuosi.rebelwar.game.object.Game;
import tech.zuosi.rebelwar.game.object.GamePlayer;
import tech.zuosi.rebelwar.message.MessageSender;
import tech.zuosi.rebelwar.util.TitleUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by iwar on 2016/9/30.
 * |-QueueManger ���й����������ý���ҷ������ȴ����л��ǿ�ʼһ������Ϸ
 |-HashMap<String,Game> Arena.name
 |-showStates() չʾGame״̬
 |-join(name)
 |-leave
 */
public class QueueManager {
    private static Map<String,Game> arenaQueue = new HashMap<>();
    private static QueueManager INSTANCE;
    private static Map<String,State> arenaState = new HashMap<>();

    private QueueManager() {}

    public enum State {
        WAIT,READY,START
    }

    //��ǰ����
    public int showNumInfo(String arenaName) {
        return arenaQueue.get(arenaName).getGamePlayerSet().size();
    }

    /**
     * -2 δ֪����
     * -1 �Ҳ���ָ������
     *  0 �����������Ѿ���ʼ��Ϸ
     *  1 �ɹ��������
     */
    public int join(GamePlayer gamePlayer,String arenaName) {
        if (!arenaQueue.containsKey(arenaName)) return -1;
        Game game = arenaQueue.get(arenaName);
        if (game.hasStart() || game.isReady()) return 0;
        boolean result = game.playerJoin(gamePlayer);
        if (result) {
            updateState(arenaName,false);
            gamePlayer.bindGame(game);
            return 1;
        }
        return -2;
    }

    public String quickJoin(GamePlayer gamePlayer) {
        Map<String,State> stateMap = getStates();
        String arenaName;
        for (Map.Entry<String,State> entry:stateMap.entrySet()) {
            if (State.WAIT == entry.getValue()) {
                arenaName = entry.getKey();
                if (1 == join(gamePlayer,arenaName)) return arenaName;
            }
        }
        return "";
    }

    // -1 û�м������
    // 0 ��Ϸ�Ѿ���ʼ��������/rebel quit�˳���Ϸ
    // 1 �ɹ��˳��ȴ�����
    public int leave(GamePlayer gamePlayer) {
        int status;
        for (Map.Entry<String,Game> entry:arenaQueue.entrySet()) {
            status=entry.getValue().playerLeave(gamePlayer);
            if (-1==status) continue;
            if (1==status) updateState(entry.getKey(),false);
            return status;
        }
        return -1;
    }

    public void updateState(String arenaName,boolean checkAndRemove) {
        if (arenaQueue.containsKey(arenaName)) {
            Game game = arenaQueue.get(arenaName);
            if (game.hasStart()) {
                arenaState.put(arenaName,State.START);
            } else if (game.isReady()) {
                arenaState.put(arenaName,State.READY);
                readyToStart(arenaName,game);
            } else {
                arenaState.put(arenaName,State.WAIT);
            }
        } else if (checkAndRemove) {
            arenaState.remove(arenaName);
        }
    }

    private void readyToStart(final String arenaName,final Game game) {
        //����ʱ30��
        final MessageSender messageSender = MessageSender.getINSTANCE();
        final Set<GamePlayer> gamePlayerSet = game.getGamePlayerSet();
        for (GamePlayer gamePlayer:gamePlayerSet) {
            messageSender.echo(gamePlayer, ChatColor.GOLD+"��Ϸ����30���ʼ");
        }
        final long stamp = System.currentTimeMillis();
        Bukkit.getScheduler().runTaskLater(RebelWar.getINSTANCE(), new Runnable() {
            @Override
            public void run() {
                for (GamePlayer gamePlayer:gamePlayerSet) {
                    messageSender.echo(gamePlayer, ChatColor.GOLD+"��Ϸ����15���ʼ");
                }
            }
        },300L);
        final int countdownId = Bukkit.getScheduler().scheduleSyncRepeatingTask(RebelWar.getINSTANCE(), new Runnable() {
            @Override
            public void run() {
                for (GamePlayer gamePlayer:gamePlayerSet) {
                    TitleUtil.sendTitle(gamePlayer.getPlayer(),"&a{����ʱ}"
                            .replace("{����ʱ}",String.valueOf((int)((System.currentTimeMillis()-stamp)/1000)-20))
                            ,null);
                }
            }
        },400L,20L);
        Bukkit.getScheduler().runTaskLater(RebelWar.getINSTANCE(), new Runnable() {
            @Override
            public void run() {
                if (game.isReady()) {
                    new GameManager(game);
                } else {
                    for (GamePlayer gamePlayer:gamePlayerSet) {
                        messageSender.echo(gamePlayer, ChatColor.RED+"�����������㣬���صȴ�����");
                    }
                }
                updateState(arenaName,false);
                Bukkit.getScheduler().cancelTask(countdownId);
            }
        },600L);
    }

    private void updateStates() {
        for (String name:arenaQueue.keySet()) {
            updateState(name,false);
        }
    }

    private boolean inQueue(String arenaName) {
        return arenaQueue.containsKey(arenaName);
    }

    public boolean addToQueue(String arenaName, Arena arena) {
        if (inQueue(arenaName)) return false;
        if (!arena.check()) return false;
        arenaQueue.put(arenaName,new Game(arena));
        arenaState.put(arenaName,State.WAIT);
        return true;
    }

    public boolean removeFromQueue(String arenaName) {
        if (inQueue(arenaName)) return false;
        arenaQueue.remove(arenaName);
        updateState(arenaName,true);
        return true;
    }

    public Map<String,State> getStates() {
        if (!arenaQueue.keySet().equals(arenaState.keySet())) updateStates();
        return arenaState;
    }

    public boolean isInStartedGame(GamePlayer gamePlayer) {
        for (String arenaName:arenaState.keySet()) {
            if (arenaState.get(arenaName) == State.START) {
                return arenaQueue.get(arenaName).isInGame(gamePlayer);
            }
        }
        return false;
    }

    public boolean isWaitInQueue(GamePlayer gamePlayer) {
        for (String arenaName:arenaState.keySet()) {
            if (arenaState.get(arenaName) != State.START) {
                return arenaQueue.get(arenaName).isInGame(gamePlayer);
            }
        }
        return false;
    }

    public static QueueManager getINSTANCE() {
        if (INSTANCE==null) INSTANCE=new QueueManager();
        return INSTANCE;
    }

}
