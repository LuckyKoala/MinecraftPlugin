package tech.zuosi.minecraft.koalavip.view;

import tech.zuosi.minecraft.koalavip.Core;
import tech.zuosi.minecraft.koalavip.constant.AssetStatus;
import tech.zuosi.minecraft.koalavip.view.template.BuffCardTemplate;

import java.util.concurrent.TimeUnit;

/**
 * Created by luckykoala on 18-3-12.
 */
public class BuffCard {
    private BuffCardTemplate template;
    private int id;
    private int remainDays;
    private long lastTimeUsed;

    public BuffCard(BuffCardTemplate template, int remainDays, long lastTimeUsed) {
        this.id = -1;
        this.template = template;
        this.remainDays = remainDays;
        this.lastTimeUsed = lastTimeUsed;
    }

    /**
     * @return 返回奖励金额或状态码
     */
    public int getReward(long now) {
        Core.getInstance().debug(() -> String.format("[Name: %s -- Id: %d]getReward.%d",
                template.getName(), id, lastTimeUsed));

        if(remainDays <= 0) return AssetStatus.EXPIRED;
        if(lastTimeUsed == 0) {
            //第一次使用
            int reward = template.getImmediately() + template.getDaily();
            recordUsage(now);
            return reward;
        } else {
            //过了多久了？
            long daySpent = TimeUnit.MILLISECONDS.toDays(now-lastTimeUsed);
            if(daySpent >= 1) {
                //OH，老伙计，是时候得到日常奖励了
                recordUsage(now);
                return template.getDaily();
            } else {
                return AssetStatus.NOT_TIME;
            }
        }
    }

    private void recordUsage(long now) {
        remainDays--;
        lastTimeUsed = now;
        Core.getInstance().getDatabaseManager().getEngine().updateBuffCard(null, this);
    }

    public BuffCardTemplate getTemplate() {
        return template;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRemainDays() {
        return remainDays;
    }

    public long getLastTimeUsed() {
        return lastTimeUsed;
    }
}
