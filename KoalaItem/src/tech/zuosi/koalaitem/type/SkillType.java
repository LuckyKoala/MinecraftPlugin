package tech.zuosi.koalaitem.type;

/**
 * Created by iwar on 2016/7/24.
 */
public class SkillType {
    public enum Trigger {
        RIGHT("�Ҽ�"),
        ATTACK("����"),
        HOLD("�ֳ�"),
        SHIFT("Ǳ��"),
        HURT("����"),
        BACKSTAB("����"),
        CRITICAL("����");

        private String display;

        Trigger(String display) {
            this.display = display;
        }

        public String getDisplay() {
            return display;
        }
    }
    public enum Effect {
        POTION_SELF("���蹥����ҩˮЧ��"),
        POTION_OTHER("���豻������ҩˮЧ��"),
        COMMAND("ִ��ָ��"),
        LIGHTNINGSTRIKE("����"),
        FIREBALL("����"),
        BLOODSUCK("��Ѫ"),
        DODGE("����"),
        CRITICAL("����"),
        THORN("����");

        private String display;

        Effect(String display) {
            this.display = display;
        }

        public String getDisplay() {
            return display;
        }
    }
}
