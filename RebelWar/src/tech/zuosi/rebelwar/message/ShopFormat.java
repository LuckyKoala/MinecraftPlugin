package tech.zuosi.rebelwar.message;

import org.bukkit.ChatColor;
import tech.zuosi.rebelwar.game.object.GamePlayer;

/**
 * Created by iwar on 2016/10/1.
 */
public class ShopFormat {

    //չʾ���Թ������Ʒ�б�
    public void showItemList(GamePlayer gamePlayer) {
        MessageSender.getINSTANCE().echo(gamePlayer,new String[]{
                ChatColor.BOLD+"                          �̵���Ʒ",
                ChatColor.GREEN+"[0]����->����[����I]��ʯ��                                 "+ChatColor.GOLD+"500��Ϸ��",
                ChatColor.GREEN+"[1]����->Ƥ����[����V] ��[����II] ����x30 ʯ�� �����ٶ�II  "+ChatColor.GOLD+"800��Ϸ��",
                ChatColor.GREEN+"[2]̹��->��ʯ��[����I] ����[����I]                          "+ChatColor.GOLD+"1000��Ϸ��",
                ChatColor.GREEN+"[3]սʿ->����[����II] ��ʯ��[����I]                         "+ChatColor.GOLD+"1200��Ϸ��",
                ChatColor.GREEN+"[4]ս��->��ʯ�ױ���[III] ��ʯ��[����II]                     "+ChatColor.GOLD+"2000��Ϸ��",
                ChatColor.GREEN+"[5]��Ʒ->���x5                                            "+ChatColor.GOLD+"10��Ϸ��",
                ChatColor.GREEN+"[6]��Ʒ->����x10                                          "+ChatColor.GOLD+"10��Ϸ��",
                ChatColor.GREEN+"[7]��Ʒ->��ƻ��x1                                         "+ChatColor.GOLD+"50��Ϸ��",
                ChatColor.GREEN+"[8]��Ʒ->Ѹ��ҩˮII(�罦)                                  "+ChatColor.GOLD+"100��Ϸ��",
                ChatColor.GREEN+"[9]��Ʒ->ĩӰ����x5                                        "+ChatColor.GOLD+"200��Ϸ��",
                ChatColor.UNDERLINE+"����/rebel shop buy <id>�����Ӧ��Ʒ"
        });
    }
}
