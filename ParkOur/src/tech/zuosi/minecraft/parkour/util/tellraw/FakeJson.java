package tech.zuosi.minecraft.parkour.util.tellraw;

/**
 * Created by iwar on 2016/9/5.
 *   Updated by LuckyKoala on 2018.9.18
 *   Actually same person -.-
 */
public class FakeJson {
    private final StringBuilder builder;

    FakeJson() {
        this.builder = new StringBuilder();
    }

    FakeJson(StringBuilder builder) {
        this.builder = builder;
    }

    private StringBuilder getBuilder() {
        return this.builder;
    }

    FakeJson beginObject() {
        builder.append('{');
        return this;
    }

    FakeJson endObject() {
        builder.append('}');
        return this;
    }

    FakeJson beginArray() {
        builder.append('[');
        return this;
    }

    FakeJson endArray() {
        builder.append(']');
        return this;
    }

    public FakeJson append(FakeJson fakeJson) {
        builder.append(fakeJson.getBuilder());
        return this;
    }

    FakeJson append(String key, String value) {
        builder.append("\"").append(key).append("\":\"").append(value).append("\"");
        return this;
    }

    FakeJson append(String key, boolean value) {
        builder.append("\"").append(key).append("\":").append(value);
        return this;
    }

    FakeJson appendRaw(String key, String value) {
        builder.append("\"").append(key).append("\":")
                .append("{\"action\":\"show_text\",\"value\":")
                .append(value)
                .append('}');
        return this;
    }

    FakeJson append(String path, String key, String value) {
        //"clickEvent":{"action":"open_url","value":"http://baidu.com"}
        builder.append("\"%event%\":{\"action\":\"%action%\",\"value\":\"%value%\"}"
                .replace("%event%",path)
                .replace("%action%",key)
                .replace("%value%",value));
        return this;
    }

    FakeJson separator() {
        builder.append(',');
        return this;
    }

    @Override
    public String toString() {
        return this.builder.toString();
    }

}
