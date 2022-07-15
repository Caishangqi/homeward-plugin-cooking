package homeward.plugin.homewardcooking.utils;

import de.tr7zw.changeme.nbtapi.NBTFile;
import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.CookingData;
import homeward.plugin.homewardcooking.pojo.CookingProcessObject;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
import homeward.plugin.homewardcooking.utils.loaders.DictionaryLoader;
import homeward.plugin.homewardcooking.utils.loaders.RecipesLoader;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.reflections.Reflections;
import redempt.redlib.itemutils.ItemBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public class CommonUtils {

    private static final String LISTENER_PACKAGE_NAME = "listeners";
    private static final String EVENTS_PACKAGE_NAME = "events";

    //我疯狂的抄@Barroit代码
    public void register() {
        String listenerPath = getPath(HomewardCooking.packageName, LISTENER_PACKAGE_NAME);
        Set<Class<? extends Listener>> classes = new Reflections(listenerPath).getSubTypesOf(Listener.class);
        classes.forEach(var -> {
            try {
                if (var.getDeclaredConstructor().getParameterCount() == 0) {
                    Listener listener = var.getDeclaredConstructor().newInstance();
                    Bukkit.getServer().getPluginManager().registerEvents(listener, HomewardCooking.getInstance());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Bukkit.getPluginManager().disablePlugin(HomewardCooking.getInstance());
            }
        });
    }

    public void loadRecipes() {
        HomewardCooking.recipesLoader = new RecipesLoader();
        HomewardCooking.recipesLoader.importRecipes();
    }

    public void loadDictionary() {
        HomewardCooking.dictionaryLoader = new DictionaryLoader();
        HomewardCooking.dictionaryLoader.importDictionary();
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

    public String toStringBlockLocationKey(Location location) {
        return location.getWorld() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
    }

    public Location toBukkitBlockLocationKey(String location, World world) {
        String[] locations = location.split(" ");
        return new Location(world, Double.parseDouble(locations[1]), Double.parseDouble(locations[2]), Double.parseDouble(locations[3]));
    }


    public void stackItemWithCondition(CookingGUI gui, ItemStack itemStack) {
        if (gui.getInventory().getItem(24) != null && gui.getInventory().getItem(24).isSimilar(itemStack)) {
            ItemStack clone = itemStack.clone();
            int amount = gui.getInventory().getItem(24).getAmount(); //3

            int targetItemsAmount = itemStack.getAmount(); //1

            if (targetItemsAmount + amount <= 64) {
                clone.setAmount(targetItemsAmount + amount);
                gui.getInventory().setItem(24, clone);
            }
        } else {
            gui.getInventory().setItem(24, itemStack);
        }
    }

    public void stackItemWithCondition(ItemStack itemStack, CookingData cookingdata) throws IOException, ClassNotFoundException {

        ItemStack itemStackInFile = (ItemStack) StreamItemsUtils.writeDecodedObject(cookingdata.getMainOutput());

        if (cookingdata.getMainOutput() != null && itemStackInFile.isSimilar(itemStack)) {
            ItemStack clone = itemStack.clone();
            int amount = itemStackInFile.getAmount(); //3
            //配方的
            int targetItemsAmount = itemStack.getAmount(); //1

            if (targetItemsAmount + amount <= 64) {
                clone.setAmount(targetItemsAmount + amount);
                cookingdata.setMainOutput(StreamItemsUtils.writeEncodedObject(clone));
            }
        } else {
            cookingdata.setMainOutput(StreamItemsUtils.writeEncodedObject(itemStack));
        }

    }

    public void saveProcessCooking() {

        HashMap<Location, CookingProcessObject> processPool = HomewardCooking.processPool;
        List<World> worlds = Bukkit.getServer().getWorlds();
        Set<Location> locations = processPool.keySet();

        worlds.forEach(K -> {
            try {
                NBTFile file = new NBTFile(new File(K.getWorldFolder().getName(), "cooking-data.nbt"));
                locations.forEach(L -> {
                    CookingProcessObject cookingProcessObject = processPool.get(L);

                    String stringBlockLocationKey = toStringBlockLocationKey(L);
                    CookingData cookingData = file.getObject(stringBlockLocationKey, CookingData.class);
                    cookingData.setProcessObject(StreamItemsUtils.writeEncodedObject(cookingProcessObject));
                    file.setObject(stringBlockLocationKey, cookingData);
                    try {
                        file.save();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                });

            } catch (Exception ignored) {

            }
        });


    }

    public void startProcessCooking() {
        HashMap<Location, CookingProcessObject> processPool = HomewardCooking.processPool;
        List<World> worlds = Bukkit.getServer().getWorlds();
        HashMap<String, CookingRecipe> loadRecipes = HomewardCooking.recipesLoader.getLoadRecipes();

        worlds.forEach(K -> {
            try {
                NBTFile file = new NBTFile(new File(K.getWorldFolder().getName(), "cooking-data.nbt"));
                Set<String> fileKeys = file.getKeys();
                fileKeys.forEach(F -> {
                    CookingData cookingData = file.getObject(F, CookingData.class);
                    Location location = toBukkitBlockLocationKey(F, K);
                    if (cookingData.getProcessObject()!=null) {

                        CookingProcessObject cookingProcessObject = null;
                        try {
                            cookingProcessObject = (CookingProcessObject) StreamItemsUtils.writeDecodedObject(cookingData.getProcessObject());
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        processPool.put(location, cookingProcessObject);
                        cookingData.setProcessObject(null);
                        file.setObject(F, cookingData);

                    }
                });

            } catch (IOException ignored) {

            }
        });
    }


}
