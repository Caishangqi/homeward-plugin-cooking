package homeward.plugin.homewardcooking.compatibilities;

import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import homeward.plugin.homewardcooking.utils.Type;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

import java.util.logging.Level;

public class CompatibilityListener implements Listener {

    public CompatibilityListener() {
        CommonUtils.log(Level.ALL, Type.LOADED, "加载尝试加载兼容性");
        Bukkit.getPluginManager().registerEvents(this, HomewardCooking.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPluginEnable(PluginEnableEvent event) {
        CommonUtils.log(Level.ALL, Type.LOADED, "插件 " + event.getPlugin().getName() + " 已加载");
        //CompatibilitiesManager.enableCompatibility(event.getPlugin().getName());
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        //CompatibilitiesManager.disableCompatibility(event.getPlugin().getName());
    }

}
