package tech.zuosi.koalaitem.skill;

/**
 * Created by iwar on 2016/8/3.
 */
public class UniSkill {
    private String timeStamp;
    private Skill skill;

    public UniSkill(String timeStamp,Skill skill) {
        this.timeStamp = timeStamp;
        this.skill = skill;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public Skill getSkill() {
        return skill;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        UniSkill u = (UniSkill) o;
        return u.getSkill().equals(getSkill()) && getTimeStamp().equals(u.getTimeStamp());
    }

    @Override
    public int hashCode() {
        return (getTimeStamp()+getSkill().getName()).hashCode();
    }
}
