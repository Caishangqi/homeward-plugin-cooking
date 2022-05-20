package homeward.plugin.homewardcooking;

import org.bukkit.plugin.java.JavaPlugin;

public final class Homewardcooking extends JavaPlugin {

    //插件主类
    public static Homewardcooking plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadDependencies();


    }

    private void loadDependencies() {
        plugin = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        unloadDependencies();
        disableTask();
    }

    private void disableTask() {
    }

    private void unloadDependencies() {
    }

}
