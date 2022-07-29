package homeward.plugin.homewardcooking.listeners.cookingprocess;

import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.events.CookingProcessEvent;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.CommonMaterial;
import homeward.plugin.homewardcooking.pojo.CookingProcessObject;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.RecipeContent;
import homeward.plugin.homewardcooking.utils.CommonUtils;
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


            HashMap<String, CookingGUI> guiPools = HomewardCooking.GUIPools;
            CookingGUI gui = guiPools.get(event.getLocationKey());

            //Deduct the quantity
            deductQuantity(event.getTargetRecipe(), gui);

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

            }

        }

    }

    /**
     * 这是复合方法，配方匹配到并且在InitialListener处理完数量匹配后
     * 需要在这里进行减少，后续可以把这个方法在初始就判断完毕，更加符合
     * 逻辑
     *
     * @param cookingRecipe 按照匹配到的配方进行删减物品数量操作
     * @param cookingGUI    玩家当前操作的GUI
     * @implSpec
     */
    private void deductQuantity(CookingRecipe cookingRecipe, CookingGUI cookingGUI) {
        for (int avaliableInputSlot : CookingGUI.avaliableInputSlots) {
            cookingRecipe.getContents().forEach(content -> {
                ItemStack objectMaterial = (ItemStack) content.getObjectMaterial();
                //获取GUI内部物品如果是空的把它换成空气
                ItemStack itemStack = cookingGUI.getInventory().getItem(avaliableInputSlot);
                if (itemStack == null)
                    itemStack = CommonMaterial.AIR.getItemStack();


                /**
                 * 这种写法后续需要优化，我不认为继续这么写能把矿物词典写出来
                 * 需要为矿物词典配方单独一个完整的工具类等工程。
                 */

                //如果匹配到了mmoitem的物品则进找到recipeContent内部存储的数量进行删减
                if (CommonUtils.getInstance().isMMOITEM(itemStack) && CommonUtils.getInstance().isSimilarMMOITEM(itemStack, objectMaterial)) {
                    doDeductAction(cookingGUI, content, avaliableInputSlot);
                    //如果匹配到了minecraft的物品则进找到recipeContent内部存储的数量进行删减
                } else if (itemStack.isSimilar(objectMaterial)) {
                    doDeductAction(cookingGUI, content, avaliableInputSlot);
                }

            });
        }

    }

    private void doDeductAction(CookingGUI cookingGUI, RecipeContent content, Integer avaliableInputSlot) {
        ItemStack item = cookingGUI.getInventory().getItem(avaliableInputSlot);
        //如果直接减少完了，那么就直接设置输入栏其中一格为空气
        if (item.getAmount() - content.getQuantity() == 0) {
            cookingGUI.getInventory().setItem(avaliableInputSlot, CommonMaterial.AIR.getItemStack());
        } else {
            //进行减少
            item.setAmount(item.getAmount() - content.getQuantity());
            cookingGUI.getInventory().setItem(avaliableInputSlot, item);
        }
    }
}
