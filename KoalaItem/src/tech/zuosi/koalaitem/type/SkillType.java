package tech.zuosi.koalaitem.type;

/**
 * Created by iwar on 2016/7/24.
 */
public class SkillType {
    public enum Trigger {
        RIGHT("右键"),
        ATTACK("攻击"),
        HOLD("手持"),
        SHIFT("潜行"),
        HURT("受伤"),
        BACKSTAB("背刺"),
        CRITICAL("暴击");

        private String display;

        Trigger(String display) {
            this.display = display;
        }

        public String getDisplay() {
            return display;
        }
    }
    public enum Effect {
        POTION_SELF("给予攻击者药水效果"),
        POTION_OTHER("给予被攻击者药水效果"),
        COMMAND("执行指令"),
        LIGHTNINGSTRIKE("落雷"),
        FIREBALL("火球"),
        BLOODSUCK("吸血"),
        DODGE("闪避"),
        CRITICAL("暴击"),
        THORN("荆棘");

        private String display;

        Effect(String display) {
            this.display = display;
        }

        public String getDisplay() {
            return display;
        }
    }
}
