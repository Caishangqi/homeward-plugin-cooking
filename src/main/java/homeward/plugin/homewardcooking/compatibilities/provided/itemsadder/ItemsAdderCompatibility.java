package homeward.plugin.homewardcooking.compatibilities.provided.itemsadder;

import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import homeward.plugin.homewardcooking.compatibilities.CompatibilityPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

public class ItemsAdderCompatibility extends CompatibilityPlugin {

    @EventHandler(ignoreCancelled = true)
    public void onItemsAdderLoadData(ItemsAdderLoadDataEvent event) {
        System.out.println("ItemsAdder把自己的物品加载成功了");
    }

    public static boolean isSimilar(ItemStack firstItems, ItemStack secondItems) {

        //先判断是不是同一个插件的物品

        //如果是则进行下一步判断 这里IA判断nbt

        return false;
    }
}
