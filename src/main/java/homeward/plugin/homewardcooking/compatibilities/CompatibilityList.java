package homeward.plugin.homewardcooking.compatibilities;

import homeward.plugin.homewardcooking.compatibilities.provided.itemsadder.ItemsAdderCompatibility;
import org.bukkit.event.Listener;

public enum CompatibilityList {

    MMOITEMS("MMOItems",false),
    ITEMSADDER("ItemsAdder",true, ItemsAdderCompatibility.class);

    private String pluginName;
    private Class<? extends Listener> compatibilityPlugin;
    private Boolean isNative;

    /**
     * 兼容列表枚举类的信息，如果兼容插件有自己的事件或者
     * 该插件使用的他的API，则需要把 {@link #isNative} 设置成 true
     * @param pluginName
     * @param isNative
     * @param compatibilityPlugin
     */
    CompatibilityList(String pluginName, Boolean isNative, Class<? extends Listener> compatibilityPlugin) {
        this.pluginName = pluginName;
        this.isNative = isNative;
        this.compatibilityPlugin = compatibilityPlugin;
    }

    CompatibilityList(String pluginName,Boolean isNative) {
        this.pluginName = pluginName;
        this.isNative = isNative;
    }

    public String getPluginName() {
        return pluginName;
    }

    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;

    }

    public Boolean getNative() {
        return isNative;
    }

    public Class<? extends Listener> getCompatibilityPlugin() {
        return compatibilityPlugin;
    }
}
