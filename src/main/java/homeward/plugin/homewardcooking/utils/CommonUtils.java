package homeward.plugin.homewardcooking.utils;

import homeward.plugin.homewardcooking.Homewardcooking;
import homeward.plugin.homewardcooking.utils.loaders.DictionaryLoader;
import homeward.plugin.homewardcooking.utils.loaders.RecipesLoader;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.reflections.Reflections;
import redempt.redlib.itemutils.ItemBuilder;

import java.util.List;
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
        Homewardcooking.recipesLoader = new RecipesLoader();
        Homewardcooking.recipesLoader.importRecipes();
    }

    public void loadDictionary() {
        Homewardcooking.dictionaryLoader = new DictionaryLoader();
        Homewardcooking.dictionaryLoader.importDictionary();
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

    public void sendPluginMessageInServer(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6协调料理锅&7] " + message));
    }

    public ItemStack buildItems(Material material, String name, List<String> lore) {
        ItemStack item = new ItemBuilder(material)
                .setName(ChatColor.translateAlternateColorCodes('&', name));
        //.setLore("Cool lore");
        for (String oneLore : lore) {
            ((ItemBuilder) item).setLore(ChatColor.translateAlternateColorCodes('&', oneLore));
        }
        return item;
    }

    public ItemStack buildItems(Material material, String name, String lore) {
        ItemStack item = new ItemBuilder(material)
                .setName(ChatColor.translateAlternateColorCodes('&', name))
                .setLore(ChatColor.translateAlternateColorCodes('&', lore));
        return item;
    }

    public ItemStack buildItems(Material material, String name) {
        ItemStack item = new ItemBuilder(material)
                .setName(ChatColor.translateAlternateColorCodes('&', name));
        return item;
    }


}
