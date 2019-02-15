package tech.zuosi.powerfulmobs.util;

/**
 * Created by iwar on 2016/4/26.
 */
public enum MobLevel {
    EASY("普通"),NORMAL("一般"),HARD("困难"),HELL("地狱"),LEGEND("传说"),EPIC("史诗"),NIGHTMARE("噩梦");
    private String name;
    MobLevel(String name) {
        this.setName(name);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
