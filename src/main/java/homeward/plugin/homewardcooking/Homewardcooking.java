package homeward.plugin.homewardcooking;

import homeward.plugin.homewardcooking.commands.MainCommand;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.listeners.cookingmaingui.CookingGUIClickListener;
import homeward.plugin.homewardcooking.listeners.cookingmaingui.CookingGUIDragListener;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import homeward.plugin.homewardcooking.utils.Type;
import me.mattstudios.mf.base.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Level;

import static org.bukkit.configuration.file.YamlConfiguration.loadConfiguration;

public final class Homewardcooking extends JavaPlugin {

    public final static String packageName = Homewardcooking.class.getPackageName();

    //插件主类
    public static Homewardcooking plugin;
    public static CommandManager commandManager;

    public static FileConfiguration config;

    //GUI 打开池
    public static HashMap<String, CookingGUI> GUIPools;

    @Override
    public void onEnable() {

        // Plugin startup logic
        loadConfigurations();
        loadDependencies();
        registerCommands();
        registerListeners();
        loadingRecipes();
        CommonUtils.getInstance().log(Level.INFO, Type.LOADED, "插件加载成功 5/5");

    }

    private void loadConfigurations() {
        //注册默认Config,没有的话创建一个
        saveDefaultConfig();
        config = getConfig();
        this.saveResource("recipe-general.yml", false);
        this.saveResource("recipe-dictionary.yml", false); //type: dictionary
        this.saveResource("message.yml", false);
        this.saveResource("database.yml", false);
    }

    private void loadingRecipes() {
        CommonUtils.getInstance().loadRecipes();
        CommonUtils.getInstance().log(Level.INFO, Type.LOADED, "配方加载成功 4/5");
    }

    private void registerListeners() {
        CommonUtils.getInstance().register();
        CommonUtils.getInstance().log(Level.INFO, Type.LOADED, "事件加载成功 3/5");
    }

    private void registerCommands() {
        commandManager.register(new MainCommand());
        CommonUtils.getInstance().log(Level.INFO, Type.LOADED, "指令加载成功 2/5");
    }

    private void loadDependencies() {
        plugin = this;
        commandManager = new CommandManager(this);
        CommonUtils.getInstance().log(Level.INFO, Type.LOADED, "依赖加载成功 1/5");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        unloadDependencies();
        disableTask();
        disableMessages();
    }

    private void disableMessages() {
        CommonUtils.getInstance().log(Level.INFO, Type.UNLOADED, "&6插件禁用成功");
    }

    private void disableTask() {
    }

    private void unloadDependencies() {
    }

    public static Homewardcooking getInstance() {
        return plugin;
    }

}
