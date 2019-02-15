package tech.zuosi.minecraft.parkour.util.tellraw;

import org.bukkit.Bukkit;

/**
 * Created by iwar on 2016/9/4.
 *   Updated by LuckyKoala on 2018.9.18
 *   Actually same person -.-
 * /tellraw @p ["",
 * {"text":"test","color":"gray","italic":true,
 *   "clickEvent":{"action":"open_url","value":"http://baidu.com"},
 *   "hoverEvent":
 *     {"action":"show_text","value":
 *         {"text":"","extra":[{"text":"2333","color":"white"}]}
 *     },"insertion":"2"},
 * {"text":"123",
 * "clickEvent":{"action":"run_command","value":"1231 1"},
 * "hoverEvent":{"action":"show_text","value":
 * {"text":"","extra":[{"text":"12312"}]
 * }},"color":"none","italic":false}]
 *
 * "bold":false,"italic":false,"underlined":false,"strikethrough":false,"obfuscated":false
 */
public class TellRawBuilder {
    private FakeJson fakeJson;
    private boolean isFirst;

    /** Usage
    TellRawBuilder tellRawBuilder = new TellRawBuilder();
        tellRawBuilder.text("/help")
                .bold()
                .onClick(ClickAction.run_command, "/help")
                .onHover(HoverAction.show_text, "执行命令")
                .endItem()
                .endAll()
                .send("iwar");
     */
    public TellRawBuilder() {
        this.fakeJson = new FakeJson().beginArray();
        this.isFirst = true;
    }

    public TellRawBuilder text(String text) {
        if (isFirst) {
            this.isFirst = false;
        } else {
            this.fakeJson = fakeJson.separator();
        }
        this.fakeJson = fakeJson.beginObject().append("text",text);
        return this;
    }

    public TellRawBuilder color(TextColor textColor) {
        this.fakeJson = fakeJson.separator().append("color",textColor.name());
        return this;
    }
    public TellRawBuilder italic() {
        this.fakeJson = fakeJson.separator().append("italic",true);
        return this;
    }
    public TellRawBuilder bold() {
        this.fakeJson = fakeJson.separator().append("bold",true);
        return this;
    }
    public TellRawBuilder underlined() {
        this.fakeJson = fakeJson.separator().append("underlined",true);
        return this;
    }
    public TellRawBuilder strikethrough() {
        this.fakeJson = fakeJson.separator().append("strikethrough",true);
        return this;
    }
    public TellRawBuilder obfuscated() {
        this.fakeJson = fakeJson.separator().append("obfuscated",true);
        return this;
    }
    public TellRawBuilder onClick(ClickAction clickAction, String value) {
        this.fakeJson = fakeJson.separator()
                .append("clickEvent",clickAction.name(),value);
        return this;
    }
    public TellRawBuilder onHover(HoverAction hoverAction, String value) {
        this.fakeJson = fakeJson.separator()
                .append("hoverEvent",hoverAction.name(),value);
        return this;
    }
    public TellRawBuilder onHover(HoverAction hoverAction, RawText rawText) {
        this.fakeJson = fakeJson.separator().appendRaw("hoverEvent",rawText.toString());
        return this;
    }

    public TellRawBuilder endItem() {
        this.fakeJson = fakeJson.endObject();
        return this;
    }

    public TellRawBuilder endAll() {
        this.fakeJson = fakeJson.endArray();
        return this;
    }

    @Override
    public String toString() {
        return fakeJson.toString();
    }

    public void send(String playerName) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tellraw " + playerName + " " + fakeJson.toString());
    }
}
