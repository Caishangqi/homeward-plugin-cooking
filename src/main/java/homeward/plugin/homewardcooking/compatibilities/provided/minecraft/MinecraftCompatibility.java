package homeward.plugin.homewardcooking.compatibilities.provided.minecraft;

import homeward.plugin.homewardcooking.compatibilities.CompatibilityPlugin;
import org.bukkit.inventory.ItemStack;

public class MinecraftCompatibility extends CompatibilityPlugin {

    public static boolean isSimilar(ItemStack firstItems, ItemStack secondItems) {
        //Vanilla method
        return firstItems.isSimilar(secondItems);
    }

}
