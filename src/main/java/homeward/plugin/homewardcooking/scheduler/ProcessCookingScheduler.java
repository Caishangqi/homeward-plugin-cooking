package homeward.plugin.homewardcooking.scheduler;

import de.tr7zw.changeme.nbtapi.NBTFile;
import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.CookingData;
import homeward.plugin.homewardcooking.pojo.CookingProcessObject;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

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
                        ItemStack objectMaterial = (ItemStack) cookingProcessObject.getCookingRecipe().getMainOutPut().getObjectMaterial();
                        CommonUtils.getInstance().stackItemWithCondition(gui, objectMaterial);

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
                    System.out.println("剩余时间" + V.getRemainTime() + "s");
                });
            }


        }, 20, 20);
    }


}
