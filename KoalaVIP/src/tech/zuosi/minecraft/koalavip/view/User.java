package tech.zuosi.minecraft.koalavip.view;

import org.bukkit.ChatColor;
import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;
import tech.zuosi.minecraft.koalavip.Core;
import tech.zuosi.minecraft.koalavip.constant.AssetStatus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Created by luckykoala on 18-3-23.
 */

public class User {
    private final String username; //用户名
    private Group group; //当前VIP组
    private List<BuffCard> buffCards;

    private long lastTimePurchasing; //最后消费时间
    private int moneySpentTotal; //共消费多少

    private int moneySpentLocked; //锁定的消费金额，用于计算是否可以恢复等级
    private int downgradeTimes; //降级次数

    public User(String username) {
        this(username,
                Core.getInstance().getTemplateManager().groupOf(0,0),
                0L, 0, 0, 0);
    }

    public User(String username, Group group, long lastTimePurchasing,
                int moneySpentLocked, int moneySpentTotal, int downgradeTimes) {
        this.username = username;
        this.group = group;
        this.lastTimePurchasing = lastTimePurchasing;
        this.moneySpentLocked = moneySpentLocked;
        this.moneySpentTotal = moneySpentTotal;
        this.downgradeTimes = downgradeTimes;
        this.buffCards = new ArrayList<>();
    }

    private void unlock(long now) {
        //最近消费过，解锁并刷新最近消费时间
        this.moneySpentLocked = 0;
        this.downgradeTimes = 0;
        this.lastTimePurchasing = now;
        Core.getInstance().getDatabaseManager().getEngine().updateUser(this);
    }

    private void lock(int downgradeTimes) {
        //降级
        this.downgradeTimes += downgradeTimes;
        Core.getInstance().getDatabaseManager().getEngine().updateUser(this);
    }

    private void upgradeGroup(Group newGroup) {
        PermissionUser user = PermissionsEx.getUser(username);
        user.removeGroup(this.group.getGroupIdentifier());
        user.addGroup(newGroup.getGroupIdentifier());

        this.group = newGroup;
        boolean result = Core.getInstance().getDatabaseManager().getEngine().saveGroup(this.username);
        Core.getInstance().debug(() -> String.format("[User: %s] saveGroup status: %s", username, result));
    }

    public void addBuffCard(BuffCard buffCard) {
        buffCards.add(buffCard);
    }

    public void refresh(int amount, long now) {
        Core.getInstance().debug(() -> String.format("User.refresh(%d, %d)", amount, now));

        moneySpentTotal += amount; //无论如何，先增加到历史总消费
        if(downgradeTimes > 0) {
            //最近降级过
            moneySpentLocked += amount;
            if(moneySpentLocked >= Core.getInstance().getConfig().getInt("Money.restore", 100)) {
                //查看是否可以恢复到历史最高用户组
                unlock(now); //先解锁才能恢复等级
                Group newGroup = Core.getInstance().getTemplateManager().groupOf(moneySpentTotal, downgradeTimes);
                if(!Objects.equals(group, newGroup)) {
                    upgradeGroup(newGroup);
                    Core.getInstance().getServer().getPlayer(this.username)
                            .sendMessage(ChatColor.YELLOW + "恭喜，您的VIP级别已经恢复至"+newGroup.getGroupName());
                    //this.group.invokeAllCommand(username, now);
                }
            } else {
                //金额不足以解锁（恢复历史最高组）
            }
        } else {
            //最近未降级
            //查看是否可以升级用户组
            unlock(now);
            Group newGroup = Core.getInstance().getTemplateManager().groupOf(moneySpentTotal, downgradeTimes);
            boolean updated = !Objects.equals(group, newGroup);
            Core.getInstance().debug(() -> String.format("oldGroup: %s, newGroup: %s, updated: %s",
                    group.getGroupName(), newGroup.getGroupName(), updated));
            if(updated) {
                upgradeGroup(newGroup);
                Core.getInstance().getServer().getPlayer(this.username)
                        .sendMessage(ChatColor.GOLD + "恭喜，您的VIP级别已经升级至"+newGroup.getGroupName());
                //this.group.invokeAllCommand(username, now);
            }
        }
    }

    public void tick(long now) {
        Core.getInstance().debug(() -> "User.tick("+now+")");

        if (lastTimePurchasing != 0) {
            int daysNotSpendMoney = (int)TimeUnit.MILLISECONDS.toDays(now-lastTimePurchasing);
            int downgradePeriod = Core.getInstance().getConfig().getInt("Period.downgrade", 30);
            int periodExpired = (daysNotSpendMoney - downgradePeriod) / downgradePeriod;

            Core.getInstance().debug(() -> String.format("daysNotSpendMoney: %d, downgradePeriod: %d",
                    daysNotSpendMoney, downgradePeriod));
            if(periodExpired > 0) {
                //n个周期未消费
                lock(periodExpired);
                Group newGroup = Core.getInstance().getTemplateManager().groupOf(moneySpentTotal, downgradeTimes);
                if(!Objects.equals(group, newGroup)) {
                    upgradeGroup(newGroup);
                    Core.getInstance().getServer().getPlayer(this.username)
                            .sendMessage(ChatColor.RED + "很遗憾，由于长期未消费，您的VIP级别已经降级至"
                                    +newGroup.getGroupName());
                    //this.group.invokeAllCommand(username, now);
                }
            } else {
                //可以提示还有多久就要降级了
            }
        } else {
            //从未消费过的用户
        }
    }

    public int getRewardFromBuff(long now) {
        int rewardSum = 0;
        Iterator<BuffCard> buffCardIterator = buffCards.iterator();
        Core.getInstance().debug(() -> "iterate buffcards length: "+buffCards.size());
        while(buffCardIterator.hasNext()) {
            BuffCard buffCard = buffCardIterator.next();
            Core.getInstance().debug(() -> "iterate buffcard: "+buffCard);
            int result = buffCard.getReward(now);
            if(AssetStatus.isOkay(result)) {
                rewardSum += result;
            } else if(result == AssetStatus.EXPIRED) {
                buffCardIterator.remove();
                //TODO 从数据库中删除
            }
        }
        return rewardSum;
    }

    public String getUsername() {
        return username;
    }

    public Group getGroup() {
        return group;
    }

    public List<BuffCard> getBuffCards() {
        return buffCards;
    }

    public long getLastTimePurchasing() {
        return lastTimePurchasing;
    }

    public int getMoneySpentTotal() {
        return moneySpentTotal;
    }

    public int getMoneySpentLocked() {
        return moneySpentLocked;
    }

    public int getDowngradeTimes() {
        return downgradeTimes;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(this == obj) return true;
        if(obj instanceof User) {
            User anotherUser = (User) obj;
            return this.username.equals(anotherUser.username);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.username.hashCode();
    }
}
