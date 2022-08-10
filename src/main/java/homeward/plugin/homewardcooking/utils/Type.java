package homeward.plugin.homewardcooking.utils;

public class Type {
    public static final Type LOADED = new Type("&7[&2+&7]", 1000);
    public static final Type UNLOADED = new Type("&7[&c-&7]", 0);
    public static final Type FATAL = new Type("&7[&cx&7]", -999);
    public static final Type WARN = new Type("&7[&6!&7]", -998);
    public static final Type LOG = new Type("&7[&3🔍&7]", -997);
    private final String name;
    private final int value;

    public Type(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
