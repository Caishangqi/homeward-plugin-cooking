package homeward.plugin.homewardcooking.listeners.cookingprocess;

import homeward.plugin.homewardcooking.Homewardcooking;
import homeward.plugin.homewardcooking.events.CookingInitialEvent;
import homeward.plugin.homewardcooking.events.CookingProcessEvent;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class CookingInitialListener implements Listener {

    @EventHandler
    public void onCookingInitial(CookingInitialEvent event) {
        CookingRecipe recipe = isPresentRecipe(event.getContainedMaterial());
        if (recipe != null) { //是否匹配配方
            //TODO 开始处理配方
            Bukkit.getServer().getPluginManager().callEvent(new CookingProcessEvent(event.getPlayer(), recipe, event.getLocationKey()));
            //特效罢了
            event.getPlayer().playSound(event.getPlayer(), Sound.BLOCK_AMETHYST_CLUSTER_BREAK, 1.0F, 1.0F);
        } else {
            event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            CommonUtils.getInstance().sendPluginMessageInServer(event.getPlayer(), "&c未找到对应配方!");
        }
    }

    public CookingRecipe isPresentRecipe(List<ItemStack> itemStacks) {

        HashMap<String, CookingRecipe> loadRecipes = Homewardcooking.recipesLoader.getLoadRecipes();
        Collection<CookingRecipe> cookingRecipes = loadRecipes.values();

        for (CookingRecipe recipe : cookingRecipes) {
            List<ItemStack> list = recipe.getObjectItems();
            if (itemStacks.containsAll(recipe.getObjectItems())) {
                return recipe;
            }
        }


        return null;
    }


}
