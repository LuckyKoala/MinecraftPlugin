package tech.zuosi.minecraft.parkour.util.tellraw;

/**
 * Created by iwar on 2016/9/6.
 * /tellraw @p ["",{"text":"test","bold":true,"clickEvent":{"action":"run_command","value":"help"},
 * "hoverEvent":{"action":"show_text","value":{"text":"","extra":[{"text":"show","color":"dark_purple"}]}}}]
 */
class RawText {
    private final FakeJson fakeJson;

    public RawText(String text,TextColor color) {
        this.fakeJson = new FakeJson(new StringBuilder(
                "{\"text\":\"\",\"extra\":[{\"text\":\"%text%\",\"color\":\"%color%\"}]}"
                        .replace("%text%",text)
                        .replace("%color%",color.name())
        ));
    }

    @Override
    public String toString() {
        return fakeJson.toString();
    }
}
