package homeward.plugin.homewardcooking.scheduler;

import de.tr7zw.changeme.nbtapi.NBTFile;
import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.Button;
import homeward.plugin.homewardcooking.pojo.CookingData;
import homeward.plugin.homewardcooking.pojo.CookingProcessObject;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.inventory.ItemStack;
import redempt.redlib.itemutils.ItemBuilder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProcessCookingScheduler {

    private static volatile ProcessCookingScheduler instance;
    private static List<Location> toDoRemove = new ArrayList<>();

    public static ProcessCookingScheduler getInstance() {

        if (instance == null) {
            synchronized (ProcessCookingScheduler.class) {
                if (instance == null) {
                    instance = new ProcessCookingScheduler();
                }
            }
        }
        return instance;

    }

    private ProcessCookingScheduler() {
    }

    public void runProcessCooking() {


        Bukkit.getScheduler().runTaskTimerAsynchronously(HomewardCooking.getInstance(), () -> {

            HashMap<Location, CookingProcessObject> processPool = HomewardCooking.processPool;
            //查看remaintime是否为0，为0移出去


            //所有配方减一时间

            if (!processPool.isEmpty()) {

                processPool.forEach((K, V) -> {
                    if (V.getRemainTime() <= 0) {
                        toDoRemove.add(K);
                    }
                });

                toDoRemove.forEach(key -> {
                    CookingProcessObject cookingProcessObject = processPool.get(key);
                    String locationKey = CommonUtils.getInstance().toStringBlockLocationKey(key);
                    if (HomewardCooking.GUIPools.containsKey(locationKey)) {
                        CookingGUI gui = HomewardCooking.GUIPools.get(locationKey);
                        gui.getInventory().setItem(Button.READY_BUTTON.getSlot(), Button.READY_BUTTON.getButton());
                        ItemStack objectMaterial = (ItemStack) cookingProcessObject.getCookingRecipe().getMainOutPut().getObjectMaterial();
                        CommonUtils.getInstance().stackItemWithCondition(gui, objectMaterial);
                        key.getWorld().playSound(key, Sound.BLOCK_DISPENSER_DISPENSE, 2.0F, 0.5F);

                    } else {
                        try {

                            NBTFile file = new NBTFile(new File(key.getWorld().getWorldFolder().getName(), "cooking-data.nbt"));
                            ItemStack objectMaterial = (ItemStack) cookingProcessObject.getCookingRecipe().getMainOutPut().getObjectMaterial();
                            CookingData cookingdata = file.getObject(locationKey, CookingData.class);
                            CommonUtils.getInstance().stackItemWithCondition(objectMaterial, cookingdata);
                            file.setObject(locationKey, cookingdata);
                            file.save();

                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    processPool.remove(key);
                });

                toDoRemove.clear();

                processPool.forEach((K, V) -> {
                    V.setRemainTime(V.getRemainTime() - 1);
                    //TODO GUI Process Alter
                    refreshGUIInfo();
                    System.out.println("剩余时间" + V.getRemainTime() + "s");
                });
            }


        }, 20, 20);
    }

    private void refreshGUIInfo() {
        HashMap<String, CookingGUI> guiPools = HomewardCooking.GUIPools;
        HashMap<Location, CookingProcessObject> processPool = HomewardCooking.processPool;

        processPool.forEach((K,V) -> {
            String stringBlockLocationKey = CommonUtils.getInstance().toStringBlockLocationKey(K);
            CookingGUI cookingGUI = guiPools.get(stringBlockLocationKey);
            ItemStack processButton = new ItemBuilder(Button.PROCESS_BUTTON.getButton())
                    .setName(ChatColor.translateAlternateColorCodes('&',Button.PROCESS_BUTTON.getName() + V.getCookingRecipe().getRecipeName())).setLore(ChatColor.translateAlternateColorCodes('&',"&6剩余时间: &7" + V.getRemainTime()));


            if (cookingGUI!=null)
                 cookingGUI.getInventory().setItem(42, processButton);
        });



    }


}
