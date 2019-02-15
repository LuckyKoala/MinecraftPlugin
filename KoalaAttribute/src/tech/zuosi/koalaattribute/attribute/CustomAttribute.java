package tech.zuosi.koalaattribute.attribute;

/**
 * Created by iwar on 2016/8/11.
 */
public class CustomAttribute {
    private String name;
    private double damage,dodge,health,critical,armor,exp,speed,blood;
    private String description;

    public CustomAttribute(String name) {
        this.name = name;
        damage = dodge = health = critical = armor = exp = speed = blood = 0D;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        if ("".equals(description) || description == null) return "Œﬁ√Ë ˆ";
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public double getDodge() {
        return dodge;
    }

    public void setDodge(double dodge) {
        this.dodge = dodge;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getCritical() {
        return critical;
    }

    public void setCritical(double critical) {
        this.critical = critical;
    }

    public double getArmor() {
        return armor;
    }

    public void setArmor(double armor) {
        this.armor = armor;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getBlood() {
        return blood;
    }

    public void setBlood(double blood) {
        this.blood = blood;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        CustomAttribute u = (CustomAttribute) o;
        if (u.getName().equals(getName())) return true;
        return false;
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }
}
