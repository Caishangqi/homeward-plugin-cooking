package homeward.plugin.homewardcooking.compatibilities;

//import io.th0rgal.oraxen.compatibilities.provided.bossshoppro.BossShopProCompatibility;
//import io.th0rgal.oraxen.compatibilities.provided.cratereloaded.CrateReloadedCompatibility;
//import io.th0rgal.oraxen.compatibilities.provided.itembridge.ItemBridgeCompatibility;
//import io.th0rgal.oraxen.compatibilities.provided.lightapi.WrappedLightAPI;
//import io.th0rgal.oraxen.compatibilities.provided.mythicmobs.MythicMobsCompatibility;
//import io.th0rgal.oraxen.compatibilities.provided.placeholderapi.PlaceholderAPICompatibility;
//import io.th0rgal.oraxen.compatibilities.provided.worldedit.WrappedWorldEdit;
//import io.th0rgal.oraxen.config.Message;
//import net.kyori.adventure.text.minimessage.Template;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import homeward.plugin.homewardcooking.utils.Type;
import org.bukkit.Bukkit;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

public class CompatibilitiesManager {

    private CompatibilitiesManager() {}

    private static final ConcurrentHashMap<String, Class<? extends CompatibilityProvider<?>>> COMPATIBILITY_PROVIDERS = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, CompatibilityProvider<?>> ACTIVE_COMPATIBILITY_PROVIDERS = new ConcurrentHashMap<>();

    public static void enableNativeCompatibilities() {
        new CompatibilityListener();
//        addCompatibility("PlaceholderAPI", PlaceholderAPICompatibility.class, true);
//        addCompatibility("BossShopPro", BossShopProCompatibility.class, true);
//        addCompatibility("CrateReloaded", CrateReloadedCompatibility.class, true);
//        addCompatibility("ItemBridge", ItemBridgeCompatibility.class, true);
//        addCompatibility("MythicMobs", MythicMobsCompatibility.class, true);
    }

    public static void disableCompatibilities() {
        ACTIVE_COMPATIBILITY_PROVIDERS.forEach((pluginName, compatibilityProvider) -> disableCompatibility(pluginName));
    }

    public static boolean enableCompatibility(final String pluginName) {
        try {
            if (!ACTIVE_COMPATIBILITY_PROVIDERS.containsKey(pluginName)
                    && COMPATIBILITY_PROVIDERS.containsKey(pluginName)
                    && hasPlugin(pluginName)) {
                final CompatibilityProvider<?> compatibilityProvider = COMPATIBILITY_PROVIDERS.
                        get(pluginName).getConstructor().newInstance();
                compatibilityProvider.enable(pluginName);
                ACTIVE_COMPATIBILITY_PROVIDERS.put(pluginName, compatibilityProvider);
                //Message.PLUGIN_HOOKS.log(Template.template("plugin", pluginName));
                CommonUtils.getInstance().log(Level.INFO, Type.LOADED,"加载兼容插件" + pluginName + "成功");
                return true;
            }
        } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static boolean disableCompatibility(final String pluginName) {
        try {
            if (!ACTIVE_COMPATIBILITY_PROVIDERS.containsKey(pluginName))
                return false;
            if (ACTIVE_COMPATIBILITY_PROVIDERS.get(pluginName).isEnabled())
                ACTIVE_COMPATIBILITY_PROVIDERS.get(pluginName).disable();
            ACTIVE_COMPATIBILITY_PROVIDERS.remove(pluginName);
            //Message.PLUGIN_UNHOOKS.log(Template.template("plugin", pluginName));
            CommonUtils.getInstance().log(Level.INFO, Type.UNLOADED,"卸载兼容插件" + pluginName + "成功");
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean addCompatibility(final String compatibilityPluginName,
                                           final Class<? extends CompatibilityProvider<?>> clazz, final boolean tryEnable) {
        try {
            if (compatibilityPluginName != null && clazz != null) {
                COMPATIBILITY_PROVIDERS.put(compatibilityPluginName, clazz);
                return !tryEnable || enableCompatibility(compatibilityPluginName);
            }
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public static boolean addCompatibility(final String compatibilityPluginName,
                                           final Class<? extends CompatibilityProvider<?>> clazz) {
        return addCompatibility(compatibilityPluginName, clazz, false);
    }

    public static CompatibilityProvider<?> getActiveCompatibility(final String pluginName) {
        return ACTIVE_COMPATIBILITY_PROVIDERS.get(pluginName);
    }

    public static Class<? extends CompatibilityProvider<?>> getCompatibility(final String pluginName) {
        return COMPATIBILITY_PROVIDERS.get(pluginName);
    }

    public static boolean isCompatibilityEnabled(final String pluginName) {
        return ACTIVE_COMPATIBILITY_PROVIDERS.containsKey(pluginName)
                && ACTIVE_COMPATIBILITY_PROVIDERS.get(pluginName).isEnabled();
    }

    public static ConcurrentHashMap<String, Class<? extends CompatibilityProvider<?>>> getCompatibilityProviders() {
        return COMPATIBILITY_PROVIDERS;
    }

    public static ConcurrentHashMap<String, CompatibilityProvider<?>> getActiveCompatibilityProviders() {
        return ACTIVE_COMPATIBILITY_PROVIDERS;
    }

    public static boolean hasPlugin(String name) {
        return Bukkit.getPluginManager().isPluginEnabled(name);
    }
}
