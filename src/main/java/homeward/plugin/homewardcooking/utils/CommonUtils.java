package homeward.plugin.homewardcooking.utils;

import de.tr7zw.changeme.nbtapi.NBTFile;
import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.compatibilities.provided.itemsadder.ItemsAdderCompatibility;
import homeward.plugin.homewardcooking.guis.CookingGUI;
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
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

import static homeward.plugin.homewardcooking.HomewardCooking.commandManager;

public class CommonUtils {

    private static final String LISTENER_PACKAGE_NAME = "listeners";
    private static final String EVENTS_PACKAGE_NAME = "events";

    private CommonUtils() {
        throw new java.lang.UnsupportedOperationException("这是一个工具类，不能被实例化");
    }

    //我疯狂的抄@Barroit代码
    public static void register() {
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

    /**
     * 如果是第一次加载配方，插件初始化 则需要判断兼容列表有无ItemsAdder
     * 如果有的话需要把这个加载配方方法让IA的LoadData事件处理
     *
     * @param init 是否是初始化加载
     */
    public static void loadRecipes(Boolean init) {
        HomewardCooking.recipesLoader = new RecipesLoader();

        if (init) {
            if (!HomewardCooking.compatibilityManager.ACTIVATED_COMPATIBILITY.containsValue(ItemsAdderCompatibility.class)) {
                HomewardCooking.recipesLoader.importRecipes();
                CommonUtils.log(Level.INFO, Type.LOADED, "配方加载成功");
            }
        } else {
            HomewardCooking.recipesLoader.importRecipes();
            CommonUtils.log(Level.INFO, Type.LOADED, "配方加载成功");
        }


    }

    public static void loadDictionary() {
        HomewardCooking.dictionaryLoader = new DictionaryLoader();
        HomewardCooking.dictionaryLoader.importDictionary();
    }

    public static String getPath(String path, String append) {
        return path + '.' + append;
    }

    public static void log(Level level, Type type, String message) {
        //Bukkit.getLogger().log(level, ChatColor.translateAlternateColorCodes('&', "[HWC] " + type.getName() + " " + message));
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', "[HWC] " + type.getName() + " " + message));

    }

    public static void sendPluginMessageInServer(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[&6协调料理锅&7] " + message));
    }

    public static ItemStack buildItems(Material material, String name, List<String> lore) {
        ItemStack item = new ItemBuilder(material)
                .setName(ChatColor.translateAlternateColorCodes('&', name));
        //.setLore("Cool lore");
        for (String oneLore : lore) {
            ((ItemBuilder) item).setLore(ChatColor.translateAlternateColorCodes('&', oneLore));
        }
        return item;
    }

    public static ItemStack buildItems(Material material, String name, String lore) {
        ItemStack item = new ItemBuilder(material)
                .setName(ChatColor.translateAlternateColorCodes('&', name))
                .setLore(ChatColor.translateAlternateColorCodes('&', lore));
        return item;
    }

    public static ItemStack buildItems(Material material, String name, Integer customModelData) {
        ItemStack item = new ItemBuilder(material)
                .setName(ChatColor.translateAlternateColorCodes('&', name))
                .setCustomModelData(customModelData);
        return item;
    }

    public static String toStringBlockLocationKey(Location location) {
        return location.getWorld() + " " + location.getBlockX() + " " + location.getBlockY() + " " + location.getBlockZ();
    }

    public static Location toBukkitBlockLocationKey(String location, World world) {
        String[] locations = location.split(" ");
        return new Location(world, Double.parseDouble(locations[1]), Double.parseDouble(locations[2]), Double.parseDouble(locations[3]));
    }


    public static void stackItemWithCondition(CookingGUI gui, ItemStack itemStack) {

        if (gui.getInventory().getItem(HomewardCooking.configurationLoader.getGUIOutputSlot()) != null && isSimilar(gui.getInventory().getItem(HomewardCooking.configurationLoader.getGUIOutputSlot()), itemStack)) {
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

    public static void stackItemWithCondition(ItemStack itemStack, CookingData cookingdata) throws IOException, ClassNotFoundException {

        ItemStack itemStackInFile = StreamItemsUtils.writeDecodedObject(cookingdata.getMainOutput(), ItemStack.class);

        if (cookingdata.getMainOutput() != null && isSimilar(itemStackInFile, itemStack)) {
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

    public static void saveProcessCooking() {

        HashMap<Location, CookingProcessObject> processPool = HomewardCooking.processPool;
        List<World> worlds = Bukkit.getServer().getWorlds();
        Set<Location> locations = processPool.keySet();

        worlds.forEach(K -> {
            try {
                NBTFile file = new NBTFile(new File(K.getWorldFolder().getName(), "cooking-data.nbt"));
                locations.forEach(L -> {
                    CookingProcessObject cookingProcessObject = processPool.get(L);

                    String stringBlockLocationKey = toStringBlockLocationKey(L);
                    if (file.hasKey(stringBlockLocationKey)) {
                        CookingData cookingData = file.getObject(stringBlockLocationKey, CookingData.class);
                        System.out.println("保存的data是" + cookingData);
                        cookingData.setProcessObject(StreamItemsUtils.writeEncodedObject(cookingProcessObject));
                        file.setObject(stringBlockLocationKey, cookingData);
                        try {
                            file.save();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }


                });


            } catch (Exception exception) {
                log(Level.WARNING, Type.FATAL, "保存正在进行的任务失败");
                exception.printStackTrace();
            }
        });

        log(Level.INFO, Type.LOADED, "成功保存正在进行的任务");


    }

    public static void startProcessCooking() {
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

                        try {
                            CookingProcessObject cookingProcessObject = StreamItemsUtils.writeDecodedObject((cookingData.getProcessObject()), CookingProcessObject.class);
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

    public static void reloadPlugin() {

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

    private static void closeAllOpenedGUI() {
        HashMap<String, CookingGUI> cachedGUI = new HashMap<>(HomewardCooking.GUIPools);
        cachedGUI.forEach((K, V) -> {
            List<Player> openedPlayers = new ArrayList<>(V.getOpenedPlayers());
            openedPlayers.forEach(HumanEntity::closeInventory);
        });
    }

    public static void reloadRecipe() {
        try {
            loadRecipes(false);
            log(Level.INFO, Type.LOADED, "菜谱配方重载成功");
        } catch (Exception exception) {
            log(Level.WARNING, Type.FATAL, "菜谱配方重载失败，问题: ");
            exception.printStackTrace();
        }
    }

    public static void reloadDictionary() {
        try {
            loadDictionary();
            log(Level.INFO, Type.LOADED, "菜谱字典重载成功");
        } catch (Exception exception) {
            log(Level.WARNING, Type.FATAL, "菜谱字典重载失败，问题: ");
            exception.printStackTrace();
        }


    }

    public static void registryReloadTabCompletion() {
        commandManager.getCompletionHandler().register("#reloadType", input -> {
            ArrayList<String> list = new ArrayList<>();
            list.add("recipe");
            list.add("dictionary");
            return list;
        });
    }

    public static List<ItemStack> getContainedItemsInData(CookingData data) {

        ArrayList<ItemStack> itemStacks = new ArrayList<>();

        try {

            ItemStack stackOne = StreamItemsUtils.writeDecodedObject(data.getSlotI(), ItemStack.class);
            ItemStack stackTwo = StreamItemsUtils.writeDecodedObject(data.getSlotII(), ItemStack.class);
            ItemStack stackThree = StreamItemsUtils.writeDecodedObject(data.getSlotIII(), ItemStack.class);
            ItemStack stackFour = StreamItemsUtils.writeDecodedObject(data.getSlotIV(), ItemStack.class);
            ItemStack outPut = StreamItemsUtils.writeDecodedObject(data.getMainOutput(), ItemStack.class);


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

    /**
     * 判断两个物品是否为同一个物品，在兼容的情况下需要判断所有兼容物品
     * 例如 mmoItems 以及 itemsAdder 还有原版物品。
     *
     * @param firstItems  第一个输入的物品
     * @param secondItems 第二个输入的物品
     * @return {@code true} 如果这两个物品是一样的
     */
    public static boolean isSimilar(ItemStack firstItems, ItemStack secondItems) {

        AtomicReference<Boolean> result = new AtomicReference<>(false);

        HomewardCooking.compatibilityManager.ACTIVATED_COMPATIBILITY.forEach((K, V) -> {
            //如果加载了ItemsAdder在这里进入ItemsAdderCompatibility.class用这个
            //类里面的判断方法，根据不同加载的类使用不同的判断。
            try {
                Method isSimilar = HomewardCooking.compatibilityManager.ACTIVATED_COMPATIBILITY.get(K).getMethod("isSimilar", ItemStack.class, ItemStack.class);
                Object methodResult = isSimilar.invoke(null, firstItems, secondItems);
                if ((Boolean) methodResult) {
                    result.set(true);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return result.get();
    }


}
