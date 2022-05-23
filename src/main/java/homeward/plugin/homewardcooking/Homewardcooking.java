package homeward.plugin.homewardcooking;

import homeward.plugin.homewardcooking.commands.MainCommand;
import me.mattstudios.mf.base.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Homewardcooking extends JavaPlugin {

    //插件主类
    public static Homewardcooking plugin;
    public static CommandManager commandManager;

    @Override
    public void onEnable() {
        // Plugin startup logic
        loadDependencies();
        registerCommand();
    }

    private void registerCommand() {
        commandManager.register(new MainCommand());
    }

    private void loadDependencies() {
        plugin = this;
        commandManager = new CommandManager(this);
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
