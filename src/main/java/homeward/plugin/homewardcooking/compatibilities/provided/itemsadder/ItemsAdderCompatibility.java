package homeward.plugin.homewardcooking.compatibilities.provided.itemsadder;

import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.compatibilities.CompatibilityPlugin;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.RecipeContent;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import homeward.plugin.homewardcooking.utils.StreamItemsUtils;
import homeward.plugin.homewardcooking.utils.Type;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public class ItemsAdderCompatibility extends CompatibilityPlugin {

    public static boolean isSimilar(ItemStack firstItems, ItemStack secondItems) {


        if (CustomStack.byItemStack(firstItems) == null || CustomStack.byItemStack(secondItems) == null) {
            return false;
        } else {

            //先判断是不是同一个插件的物品
            NBTItem nbtItem1 = new NBTItem(firstItems);
            NBTItem nbtItem2 = new NBTItem(secondItems);

            String id1 = nbtItem1.getCompound("itemsadder").getString("id");
            String namespace1 = nbtItem1.getCompound("itemsadder").getString("namespace");
            String nameSpaceAndId1 = namespace1 + ":" + id1;

            String id2 = nbtItem2.getCompound("itemsadder").getString("id");
            String namespace2 = nbtItem2.getCompound("itemsadder").getString("namespace");
            String nameSpaceAndId2 = namespace2 + ":" + id2;

            return Objects.equals(nameSpaceAndId1, nameSpaceAndId2);


        }


    }

    public static void saveItemAdderCompatibility(LinkedHashMap<Integer, ItemStack> itemStackList) {

        itemStackList.forEach((K, V) -> {
            if (!itemStackList.get(K).getType().isAir())
                itemStackList.put(K, StreamItemsUtils.deserializeItem(StreamItemsUtils.serializeItem(itemStackList.get(K))));

        });

    }

    public static void saveItemAdderCompatibility(List<RecipeContent> recipeContentList) {
        recipeContentList.forEach(K -> {
            if (!K.getObjectMaterial().getType().isAir()) {
                K.setObjectMaterial(StreamItemsUtils.deserializeItem(StreamItemsUtils.serializeItem(K.getObjectMaterial())));
            }

        });
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemsAdderLoadData(ItemsAdderLoadDataEvent event) {
        HomewardCooking.recipesLoader.importRecipes();
        CommonUtils.log(Level.INFO, Type.LOADED, "配方加载成功 (顺序改变因为 &6ItemsAdder&7)");
    }
}
