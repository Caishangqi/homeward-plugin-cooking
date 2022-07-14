package homeward.plugin.homewardcooking.listeners.cookingprocess;

import homeward.plugin.homewardcooking.Homewardcooking;
import homeward.plugin.homewardcooking.events.CookingProcessEvent;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.CookingProcessObject;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CookingProcessListener implements Listener {

    @EventHandler
    public void onCookingProcess(CookingProcessEvent event) {
        //TODO 开始处理配方
        CookingRecipe targetRecipe = event.getTargetRecipe();

        ItemStack targetItems = (ItemStack) targetRecipe.getMainOutPut().getObjectMaterial();

        if (event.getWhoCalled().getOpenInventory().getTopInventory().getHolder() instanceof CookingGUI) {
            //如果配方时间等于0则直接产出配方
            if (targetRecipe.getTotalRequiredTimes() == 0) {
                HashMap<String, CookingGUI> guiPools = Homewardcooking.GUIPools;
                CookingGUI cookingGUI = guiPools.get(event.getLocationKey());
                cookingGUI.getInventory().setItem(24, targetItems);
                System.out.println(event.getLocationKey());
                System.out.println(event.getWhoCalled().getLocation());
            } else {
                //开始建立烹饪时间任务并使用调度器每秒减少所需时间
                String location = event.getLocationKey();
                String[] locations = location.split(" ");
                Location exactPotLocation = new Location(event.getWhoCalled().getWorld(),Double.parseDouble(locations[1]),Double.parseDouble(locations[2]),Double.parseDouble(locations[3]));
                CookingProcessObject processObject = new CookingProcessObject();
                processObject.setCookingRecipe(event.getTargetRecipe());
                processObject.setRemainTime(event.getTargetRecipe().getTotalRequiredTimes());
                Homewardcooking.processPool.put(exactPotLocation,processObject); //放入池子中

            }

        }

    }
}
