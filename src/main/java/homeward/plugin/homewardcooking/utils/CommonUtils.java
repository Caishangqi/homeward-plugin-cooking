package homeward.plugin.homewardcooking.utils;

import de.tr7zw.changeme.nbtapi.NBTFile;
import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.Button;
import homeward.plugin.homewardcooking.pojo.CookingData;
import homeward.plugin.homewardcooking.pojo.CookingProcessObject;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
import homeward.plugin.homewardcooking.utils.loaders.ConfigurationLoader;
import homeward.plugin.homewardcooking.utils.loaders.DictionaryLoader;
import homeward.plugin.homewardcooking.utils.loaders.RecipesLoader;
import org.bukkit.*;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.reflections.Reflections;
import redempt.redlib.itemutils.ItemBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

import static homeward.plugin.homewardcooking.HomewardCooking.commandManager;

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

    public ItemStack buildItems(Material material, String name, Integer customModelData) {
        ItemStack item = new ItemBuilder(material)
                .setName(ChatColor.translateAlternateColorCodes('&', name))
                .setCustomModelData(customModelData);
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

        if (gui.getInventory().getItem(HomewardCooking.configurationLoader.getGUIOutputSlot()) != null && gui.getInventory().getItem(HomewardCooking.configurationLoader.getGUIOutputSlot()).isSimilar(itemStack)) {
            ItemStack clone = itemStack.clone();
            int amount = gui.getInventory().getItem(HomewardCooking.configurationLoader.getGUIOutputSlot()).getAmount(); //3

            int targetItemsAmount = itemStack.getAmount(); //1

            if (targetItemsAmount + amount <= 64) {
                clone.setAmount(targetItemsAmount + amount);
                gui.getInventory().setItem(HomewardCooking.configurationLoader.getGUIOutputSlot(), clone);
            }
        } else {
            gui.getInventory().setItem(HomewardCooking.configurationLoader.getGUIOutputSlot(), itemStack);
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


            } catch (Exception exception) {
                log(Level.WARNING, Type.FATAL, "保存正在进行的任务失败");
                exception.printStackTrace();
            }
        });

        log(Level.INFO, Type.LOADED, "成功保存正在进行的任务");


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
                    if (cookingData.getProcessObject() != null) {

                        CookingProcessObject cookingProcessObject = null;
                        try {
                            cookingProcessObject = (CookingProcessObject) StreamItemsUtils.writeDecodedObject(cookingData.getProcessObject());
                            processPool.put(location, cookingProcessObject);
                            cookingData.setProcessObject(null);
                            file.setObject(F, cookingData);
                            file.save();
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }

                    }
                });


            } catch (Exception exception) {
                log(Level.WARNING, Type.FATAL, "加载保存的的任务失败");
                exception.printStackTrace();
            }
        });
        log(Level.INFO, Type.LOADED, "成功加载保存的任务");
    }

    public void reloadPlugin() {

        try {
            closeAllOpenedGUI();
            reloadDictionary();
            reloadRecipe();
            HomewardCooking.configurationLoader = new ConfigurationLoader();
            log(Level.INFO, Type.LOADED, "插件重载成功！");
        } catch (Exception exception) {
            log(Level.WARNING, Type.FATAL, "插件重载失败，问题: ");
            exception.printStackTrace();
        }


    }

    private void closeAllOpenedGUI() {
        HashMap<String, CookingGUI> cachedGUI = new HashMap<>(HomewardCooking.GUIPools);
        cachedGUI.forEach((K, V) -> {
            List<Player> openedPlayers = new ArrayList<>(V.getOpenedPlayers());
            openedPlayers.forEach(HumanEntity::closeInventory);
        });
    }

    public void reloadRecipe() {
        try {
            loadRecipes();
            log(Level.INFO, Type.LOADED, "菜谱配方重载成功");
        } catch (Exception exception) {
            log(Level.WARNING, Type.FATAL, "菜谱配方重载失败，问题: ");
            exception.printStackTrace();
        }
    }

    public void reloadDictionary() {
        try {
            loadDictionary();
            log(Level.INFO, Type.LOADED, "菜谱字典重载成功");
        } catch (Exception exception) {
            log(Level.WARNING, Type.FATAL, "菜谱字典重载失败，问题: ");
            exception.printStackTrace();
        }


    }

    public void registryReloadTabCompletion() {
        commandManager.getCompletionHandler().register("#reloadType", input -> {
            ArrayList<String> list = new ArrayList<>();
            list.add("recipe");
            list.add("dictionary");
            return list;
        });
    }

    public List<ItemStack> getContainedItemsInData(CookingData data) {

        ArrayList<ItemStack> itemStacks = new ArrayList<>();

        try {

            ItemStack stackOne = (ItemStack) StreamItemsUtils.writeDecodedObject(data.getSlotI());
            ItemStack stackTwo = (ItemStack) StreamItemsUtils.writeDecodedObject(data.getSlotII());
            ItemStack stackThree = (ItemStack) StreamItemsUtils.writeDecodedObject(data.getSlotIII());
            ItemStack stackFour = (ItemStack) StreamItemsUtils.writeDecodedObject(data.getSlotIV());
            ItemStack outPut = (ItemStack) StreamItemsUtils.writeDecodedObject(data.getMainOutput());


            itemStacks.add(stackOne);
            itemStacks.add(stackTwo);
            itemStacks.add(stackThree);
            itemStacks.add(stackFour);
            itemStacks.add(outPut);

            return itemStacks;

        } catch (Exception exception) {
            exception.printStackTrace();
        }

        return itemStacks;

    }


}
