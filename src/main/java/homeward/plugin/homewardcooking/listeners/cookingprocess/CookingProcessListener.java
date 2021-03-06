package homeward.plugin.homewardcooking.listeners.cookingprocess;

import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.events.CookingProcessEvent;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.CommonMaterial;
import homeward.plugin.homewardcooking.pojo.CookingProcessObject;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class CookingProcessListener implements Listener {

    @EventHandler
    public void onCookingProcess(CookingProcessEvent event) {
        //TODO 开始处理配方
        CookingRecipe targetRecipe = event.getTargetRecipe();

        ItemStack targetItems = (ItemStack) targetRecipe.getMainOutPut().getObjectMaterial();

        if (event.getWhoCalled().getOpenInventory().getTopInventory().getHolder() instanceof CookingGUI) {



            HashMap<String, CookingGUI> guiPools = HomewardCooking.GUIPools;
            CookingGUI gui = guiPools.get(event.getLocationKey());

            //Deduct the quantity
            deductQuantity(event.getTargetRecipe(),gui);

            //如果配方时间等于0则直接产出配方
            if (targetRecipe.getTotalRequiredTimes() == 0) {

                CommonUtils.getInstance().stackItemWithCondition(gui, targetItems);

            } else {
                //开始建立烹饪时间任务并使用调度器每秒减少所需时间
                String location = event.getLocationKey();
                String[] locations = location.split(" ");
                Location exactPotLocation = new Location(event.getWhoCalled().getWorld(), Double.parseDouble(locations[1]), Double.parseDouble(locations[2]), Double.parseDouble(locations[3]));
                CookingProcessObject processObject = new CookingProcessObject();
                processObject.setCookingRecipe(event.getTargetRecipe());
                processObject.setRemainTime(event.getTargetRecipe().getTotalRequiredTimes());

                HomewardCooking.processPool.put(exactPotLocation, processObject); //放入池子中
                System.out.println();

            }

        }

    }

    private void deductQuantity(CookingRecipe cookingRecipe, CookingGUI cookingGUI) {
        for (int avaliableInputSlot : CookingGUI.avaliableInputSlots) {
            cookingRecipe.getContents().forEach(content -> {
                ItemStack objectMaterial = (ItemStack) content.getObjectMaterial();
                //获取GUI内部物品如果是空的把它换成空气
                ItemStack itemStack = cookingGUI.getInventory().getItem(avaliableInputSlot);
                if (itemStack == null)
                    itemStack = CommonMaterial.AIR.getItemStack();

                if (itemStack.isSimilar(objectMaterial)) {
                    ItemStack item = cookingGUI.getInventory().getItem(avaliableInputSlot);
                    if (item.getAmount() - content.getQuantity() == 0) {
                        cookingGUI.getInventory().setItem(avaliableInputSlot,CommonMaterial.AIR.getItemStack());
                    } else {
                        item.setAmount(item.getAmount() - content.getQuantity());
                        cookingGUI.getInventory().setItem(avaliableInputSlot,item);
                    }

                }
            });
        }

    }
}
