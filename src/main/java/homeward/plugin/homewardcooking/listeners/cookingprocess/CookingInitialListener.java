package homeward.plugin.homewardcooking.listeners.cookingprocess;

import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.events.CookingInitialEvent;
import homeward.plugin.homewardcooking.events.CookingProcessEvent;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.RecipeContent;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CookingInitialListener implements Listener {

    @EventHandler
    public void onCookingInitial(CookingInitialEvent event) {
        CookingRecipe recipe = isPresentRecipe(event.getContainedMaterial());

        Bukkit.getScheduler().runTaskAsynchronously(HomewardCooking.getInstance(), new Runnable() {
            @Override
            public void run() {

            }
        });

        if (HomewardCooking.processPool.containsKey(CommonUtils.getInstance().toBukkitBlockLocationKey(event.getLocationKey(),event.getPlayer().getWorld()))) {
            event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_PISTON_CONTRACT, 1.0F, 2.0F);
            CommonUtils.getInstance().sendPluginMessageInServer(event.getPlayer(), "&c当前已经有一个正在进行的配方了");
        } else if (recipe != null && isNumberValid(recipe,event.getContainedMaterial(),HomewardCooking.GUIPools.get(event.getLocationKey()))) {
            //TODO 开始处理配方
            Bukkit.getServer().getPluginManager().callEvent(new CookingProcessEvent(event.getPlayer(), recipe, event.getLocationKey()));
            //特效罢了
            event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 1.0F, 1.0F);
        } else {
            event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            CommonUtils.getInstance().sendPluginMessageInServer(event.getPlayer(), "&c未找到对应配方!");
        }

    }

    /**
     *
     */
    private boolean isNumberValid(CookingRecipe cookingRecipe, List<ItemStack> itemStacks, CookingGUI cookingGUI) {
        AtomicInteger totalMatch = new AtomicInteger();

        if (cookingRecipe!=null) {
            List<RecipeContent> recipeContents = cookingRecipe.getContents();
            recipeContents.forEach(recipeContent->{
                ItemStack recipeItemStack = (ItemStack) recipeContent.getObjectMaterial();
                //TODO
                itemStacks.forEach(preparedItem ->{
                    //System.out.println("(!)" + preparedItem.getType() + "-" + preparedItem.getAmount() + "<>" + recipeContent.getMaterial() + "-" + recipeContent.getQuantity());
                    if (recipeItemStack.isSimilar(preparedItem) && preparedItem.getAmount()>= recipeContent.getQuantity()) {
                        //System.out.println("preparedItem: " + preparedItem.getAmount() + preparedItem.getType());
                        totalMatch.set(totalMatch.get() + 1);
                    }
                });
            });
        }

        //System.out.println(totalMatch.get());
        return totalMatch.get() == 4;
    }

    public CookingRecipe isPresentRecipe(List<ItemStack> itemStacks) {
        HashMap<String, CookingRecipe> loadRecipes = HomewardCooking.recipesLoader.getLoadRecipes();
        final CookingRecipe[] cookingRecipe = {null};

        loadRecipes.forEach((KEY,COOKINGRECIPE) -> {
            AtomicInteger totalMatch = new AtomicInteger();
            COOKINGRECIPE.getContents().forEach(recipeContent -> {
                ItemStack objectMaterial = (ItemStack) recipeContent.getObjectMaterial();
                itemStacks.forEach(itemStack -> {
                    if (objectMaterial.isSimilar(itemStack))
                        totalMatch.set(totalMatch.get() + 1);
                });
            });
            if (totalMatch.get() == 4)
                cookingRecipe[0] = COOKINGRECIPE;
        });







        return cookingRecipe[0];
    }


}
