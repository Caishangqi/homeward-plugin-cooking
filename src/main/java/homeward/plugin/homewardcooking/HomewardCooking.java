package homeward.plugin.homewardcooking;

import homeward.plugin.homewardcooking.commands.MainCommand;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.CookingProcessObject;
import homeward.plugin.homewardcooking.scheduler.ProcessCookingScheduler;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import homeward.plugin.homewardcooking.utils.Type;
import homeward.plugin.homewardcooking.utils.loaders.ConfigurationLoader;
import homeward.plugin.homewardcooking.utils.loaders.DictionaryLoader;
import homeward.plugin.homewardcooking.utils.loaders.RecipesLoader;
import me.mattstudios.mf.base.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.logging.Level;

public final class HomewardCooking extends JavaPlugin {

    public final static String packageName = HomewardCooking.class.getPackageName();

    //插件主类
    public static HomewardCooking plugin;
    public static CommandManager commandManager;

    public static FileConfiguration config;
    public static RecipesLoader recipesLoader;
    public static DictionaryLoader dictionaryLoader;
    public static ConfigurationLoader configurationLoader;

    //GUI 打开池
    public static HashMap<String, CookingGUI> GUIPools = new HashMap<>();
    //任务调度池
    public static HashMap<Location, CookingProcessObject> processPool = new HashMap<>();

    @Override
    public void onEnable() {

        // Plugin startup logic
        loadDependencies();
        registerCommands();
        registerListeners();
        loadConfigurations();
        loadingRecipes();
        loadingScheduler();
        loadingCookingProcess();
        CommonUtils.getInstance().log(Level.INFO, Type.LOADED, "插件加载成功 5/5");

    }

    private void loadingCookingProcess() {
        CommonUtils.getInstance().startProcessCooking();
    }

    private void loadingScheduler() {
        ProcessCookingScheduler.getInstance().runProcessCooking();
    }

    private void loadConfigurations() {
        //注册默认Config,没有的话创建一个
        saveDefaultConfig();
        config = getConfig();
        this.saveResource("recipes/recipe-general.yml", false);
        this.saveResource("dictionary/dictionary.yml", false); //type: dictionary
        this.saveResource("message.yml", false);
        this.saveResource("database.yml", false);

        configurationLoader = new ConfigurationLoader();
        configurationLoader.loadConfiguration();


    }

    private void loadingRecipes() {
        CommonUtils.getInstance().loadRecipes();
        CommonUtils.getInstance().loadDictionary();
        CommonUtils.getInstance().log(Level.INFO, Type.LOADED, "配方加载成功 4/5");
    }

    private void registerListeners() {
        CommonUtils.getInstance().register();
        CommonUtils.getInstance().log(Level.INFO, Type.LOADED, "事件加载成功 3/5");
    }

    private void registerCommands() {
        CommonUtils.getInstance().registryReloadTabCompletion();
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
        unloadScheduler();
        saveProcessCooking();
        unloadDependencies();
        disableTask();
        disableMessages();
    }

    private void unloadScheduler() {
        Bukkit.getScheduler().cancelTasks(HomewardCooking.getInstance());
    }

    private void saveProcessCooking() {
        CommonUtils.getInstance().saveProcessCooking();
    }

    private void disableMessages() {
        CommonUtils.getInstance().log(Level.INFO, Type.UNLOADED, "&6插件禁用成功");
    }

    private void disableTask() {
    }

    private void unloadDependencies() {
    }

    public static HomewardCooking getInstance() {
        return plugin;
    }

}
