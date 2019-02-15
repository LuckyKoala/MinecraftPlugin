package tech.zuosi.minecraft.koalavip.view.template;

/**
 * Created by luckykoala on 18-3-26.
 */
public class BuffCardTemplate {
    private final String name;
    private final int price, days, immediately, daily;

    public BuffCardTemplate(String name, int price, int days, int immediately, int daily) {
        this.name = name;
        this.price = price;
        this.days = days;
        this.immediately = immediately;
        this.daily = daily;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getDays() {
        return days;
    }

    public int getImmediately() {
        return immediately;
    }

    public int getDaily() {
        return daily;
    }
}
