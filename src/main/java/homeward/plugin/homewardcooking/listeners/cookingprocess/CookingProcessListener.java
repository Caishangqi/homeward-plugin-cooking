package homeward.plugin.homewardcooking.listeners.cookingprocess;

import homeward.plugin.homewardcooking.Homewardcooking;
import homeward.plugin.homewardcooking.events.CookingProcessEvent;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
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
            HashMap<String, CookingGUI> guiPools = Homewardcooking.GUIPools;
            CookingGUI cookingGUI = guiPools.get(event.getLocationKey());
            cookingGUI.getInventory().setItem(24, targetItems);
            System.out.println("确实开始了" + targetRecipe.getMainOutPut().getType());
        } else {
            System.out.println("???");
        }

    }


}
