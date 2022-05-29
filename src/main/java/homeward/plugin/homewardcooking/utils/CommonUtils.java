package homeward.plugin.homewardcooking.utils;

import homeward.plugin.homewardcooking.Homewardcooking;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.reflections.Reflections;

import java.util.Set;
import java.util.logging.Level;

public class CommonUtils {

    private static final String LISTENER_PACKAGE_NAME = "listeners";
    private static final String EVENTS_PACKAGE_NAME = "events";

    //我疯狂的抄@Barroit代码
    public void register() {
        String listenerPath = getPath(Homewardcooking.packageName, LISTENER_PACKAGE_NAME);
        Set<Class<? extends Listener>> classes = new Reflections(listenerPath).getSubTypesOf(Listener.class);
        classes.forEach(var -> {
            try {
                if (var.getDeclaredConstructor().getParameterCount() == 0) {
                    Listener listener = var.getDeclaredConstructor().newInstance();
                    Bukkit.getServer().getPluginManager().registerEvents(listener, Homewardcooking.getInstance());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(Homewardcooking.getInstance());
            }
        });
    }

    public void loadRecipes() {

    }

    public static CommonUtils getInstance() {
        return new CommonUtils();
    }

    public String getPath(String path, String append) {
        return path + '.' + append;
    }

    public void log(Level level, Type type, String message) {
        //Bukkit.getLogger().log(level, ChatColor.translateAlternateColorCodes('&', "[HWC] " + type.getName() + " " + message));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[HWC] " + type.getName() + " " + message));

    }


}
