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

    //�򵥷�װʹ��֧��GamePlayer
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

    //���ͽ�ɫ������ʾ��Ϣ
    public void sendRoleInit(Set<GamePlayer> gamePlayerSet) {
        for (GamePlayer gamePlayer:gamePlayerSet) {
            GamePlayer.Status status = gamePlayer.getStatus();
            echo(gamePlayer,ChatColor.GREEN+"��ɫ��������ϣ������ֵĽ�ɫΪ[status]"
                    .replace("status",ChatColor.GOLD+status.getDescription()));
        }
    }

    //�㲥��һ�׶���Ϸ��ʼ��ʾ��Ϣ
    public void broadcastGameAStart(Set<GamePlayer> gamePlayerSet) {
        echoAll(gamePlayerSet,new String[]{
                ChatColor.LIGHT_PURPLE + "��һ�׶ο�ʼ~",
                ChatColor.RED + "���׶�Ŀ��:",
                ChatColor.GRAY + "����: ɱ��������ͨ���/��ֹ��ͨ��ҷ���Կ��",
                ChatColor.GRAY + "��ͨ���: ɱ�����з���/�ҵ�������һ��ʮ��Կ��",
                ChatColor.GOLD + "����Կ�׶������ú󽫿�����������"
        });
    }

    //�㲥�ڶ��׶���Ϸ��ʼ��ʾ��Ϣ
    public void broadcastGameBStart(Set<GamePlayer> gamePlayerSet) {
        echoAll(gamePlayerSet,new String[]{
                ChatColor.LIGHT_PURPLE + "�ڶ��׶ο�ʼ~",
                ChatColor.RED + "���׶�Ŀ��:",
                ChatColor.GRAY + "����: ɱ��������ͨ���",
                ChatColor.GRAY + "��ͨ���: ɱ�����з���",
                ChatColor.GOLD + "����һ����Ӫ��������Ϊ0ʱ��Ϸ����"
        });
    }

    //��Ϸ����ʱ��ʾ mode 
    public void broadcastGameTime(Set<GamePlayer> gamePlayerSet,int mode,String... arg) {
        //
    }

    //��Ϸ������������ͳ����Ϣ
    public void sendCurrentStat(GamePlayer gamePlayer) {
        /*
        * &2===============================================================

	      &f&l��ɱ:{��ɱ����} ����ɱ1���3��Ϸ�ң�
	      &f&l������Ϸ�����1��Ϸ��
	      &f&l���:{��Ϸ��} ��Ϸ��

	      &2===============================================================
        */
        GameStat gameStat = gamePlayer.getGameStat();
        echo(gamePlayer,new String[]{
                ChatColor.translateAlternateColorCodes('&',
                        "&2==============================================================="),
                "\n",
                ChatColor.translateAlternateColorCodes('&',
                        "&f&l��ɱ:{��ɱ����} (��ɱ1���3��Ϸ��)"
                                .replace("{��ɱ����}",String.valueOf(gameStat.getKillCount()))),
                ChatColor.translateAlternateColorCodes('&',
                        "&f&l������Ϸ�����1��Ϸ��"),
                ChatColor.translateAlternateColorCodes('&',
                        "&f&l���:{��Ϸ��} ��Ϸ��"
                                .replace("{��ɱ����}",String.valueOf(gameStat.getCoinCount()))),
                "\n",
                ChatColor.translateAlternateColorCodes('&',
                        "&2===============================================================")
        });
    }

    //��һ��Կ��
    public void broadcastKeyPlace(Set<GamePlayer> gamePlayerSet) {
        echoAll(gamePlayerSet,ChatColor.GREEN+"һ��Կ���Ѿ��ɹ�����");
    }
}
