package tech.zuosi.koalaprefix.data;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by iwar on 2017/1/26.
 */
public class Prefix {
    private String name;
    private List<String> description;
    private String model;
    private PrefixState currentState;
    private Aquire_Mode aquire_mode;
    private int mode_value;

    public Prefix(String name,List<String> description,String model,Aquire_Mode aquire_mode,int mode_value) {
        this.name = name;
        this.description = description;
        this.model = model;
        this.currentState = aquire_mode.getDefalutOwnState();
        this.aquire_mode = aquire_mode;
        this.mode_value = mode_value;
    }

    public Prefix(String name,List<String> description,String model,PrefixState currentState,Aquire_Mode aquire_mode,int mode_value) {
        this.name = name;
        this.description = description;
        this.model = model;
        this.currentState = currentState;
        this.aquire_mode = aquire_mode;
        this.mode_value = mode_value;
    }

    public enum Aquire_Mode {
        GIVE_ONLY(PrefixState.UNOWNED),
        BUY_POINT(PrefixState.UNOWNED_CANBUY);

        private PrefixState defalutOwnState;

        Aquire_Mode(PrefixState defalutOwnState) {
            this.defalutOwnState = defalutOwnState;
        }

        public PrefixState getDefalutOwnState() {
            return defalutOwnState;
        }

        @Override
        public String toString() {
            return this.name();
        }
    }

    public enum PrefixState {
        UNOWNED("未拥有"),
        UNOWNED_CANBUY("未拥有 可购买"),
        OWNED("已拥有"),
        OWNED_USING("正在使用");

        private String info;

        PrefixState(String info) {
            this.info = info;
        }

        public String getInfo() {
            return info;
        }
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(o==null || !(o instanceof Prefix)) return false;
        if(this == o) return true;
        Prefix po = (Prefix)o;
        return this.name.equals(po.name);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        StringBuilder descBuilder = new StringBuilder();
        descBuilder.append('@');
        for(String str:description) {
            descBuilder.append(str).append('-');
        }
        descBuilder.append('@');
        return builder.append(name).append('-')
                .append(descBuilder.toString()).append('-') //description is list<String>
                .append(model).append('-')
                .append(currentState.name()).append('-')
                .append(aquire_mode.name()).append('-')
                .append(mode_value).append('-').toString();
    }

    public static Prefix loadFromString(String data) {
        String[] dataArray = data.split("@");
        String name = dataArray[0].replace('-',' ');
        String[] descriptionParts = dataArray[1].split("-");
        String[] afterDescription = dataArray[2].split("-");
        List<String> descList = new ArrayList<>();
        for(String str:descriptionParts) {
            descList.add(str);
        }

        System.out.println(afterDescription[1]+" "+afterDescription[2]+" "+afterDescription[3]
                +" "+afterDescription[4]);

        return new Prefix(name,descList,afterDescription[1],
                PrefixState.valueOf(afterDescription[2]),Aquire_Mode.valueOf(afterDescription[3]),
                Integer.parseInt(afterDescription[4]));
    }

    public String covertToMessage() {
        return this.toString();
    }

    public List<String> generateLore(PrefixState state) {
        String stateInfo = state==null ? this.currentState.getInfo() : state.getInfo();
        List<String> lore = description;
        lore.add(0,name);
        lore.add("");
        lore.add(stateInfo);
        lore.stream().map(x-> ChatColor.translateAlternateColorCodes('&',x)).collect(Collectors.toList());
        return lore;
    }

    public String getModel() {
        return this.model;
    }

    public PrefixState getCurrentState() {
        return this.currentState;
    }

    public void setCurrentState(PrefixState currentState) {
        this.currentState = currentState;
    }

    public Aquire_Mode getAquire_mode() {
        return this.aquire_mode;
    }

    public int getMode_value() {
        return this.mode_value;
    }

    public String getName() {
        return this.name;
    }
}
