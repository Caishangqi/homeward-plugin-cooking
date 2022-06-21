package homeward.plugin.homewardcooking.pojo;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum CommonMaterial {

    AIR(new ItemStack(Material.AIR));

    private final ItemStack itemStack;

    CommonMaterial(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

}
