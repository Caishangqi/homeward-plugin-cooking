package homeward.plugin.homewardcooking.compatibilities;

import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.compatibilities.provided.minecraft.MinecraftCompatibility;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import homeward.plugin.homewardcooking.utils.Type;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class CompatibilityManager {

    private final CompatibilityListener compatibilityListener = new CompatibilityListener();

    public final HashMap<Plugin, Class<? extends Listener>> ACTIVATED_COMPATIBILITY = new HashMap<>();
    private final HashMap<String, Class<? extends Listener>> UNLOADED_COMPATIBILITY = new HashMap<>();
    private final String COMPATIBILITY_PACKAGE_NAME = "compatibilities.provided";

    public CompatibilityManager() {
        addUnloadedCompatibilityList();
        addActivatedCompatibilityList();
        registerCompatibility();
    }

    private void addActivatedCompatibilityList() {
        UNLOADED_COMPATIBILITY.forEach((K, V) -> {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(K);
            ACTIVATED_COMPATIBILITY.put(plugin, V);
        });
        //Register Original Compatibility
        ACTIVATED_COMPATIBILITY.put(HomewardCooking.getInstance(), MinecraftCompatibility.class);
    }

    private void addUnloadedCompatibilityList() {
        Arrays.stream(CompatibilityList.values()).toList().forEach(K -> {
            if (Bukkit.getPluginManager().isPluginEnabled(K.getPluginName()) && K.getNative()) {
                UNLOADED_COMPATIBILITY.put(K.getPluginName(), K.getCompatibilityPlugin());
            }
        });
    }

    private void registerCompatibility() {
        ACTIVATED_COMPATIBILITY.forEach((K, V) -> {
            try {
                if (V.getDeclaredConstructor().getParameterCount() == 0) {
                    Listener listener = V.getDeclaredConstructor().newInstance();
                    Bukkit.getServer().getPluginManager().registerEvents(listener, HomewardCooking.getInstance());
                    CommonUtils.log(Level.ALL, Type.LOADED, "兼容插件" + K.getName() + "成功加载");
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }

        });
    }


}
