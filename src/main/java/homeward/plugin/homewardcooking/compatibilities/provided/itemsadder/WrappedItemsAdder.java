package homeward.plugin.homewardcooking.compatibilities.provided.itemsadder;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class WrappedItemsAdder {
    private final String nameSpace;
    private final String id;
    private final String nameSpacedId;

    public WrappedItemsAdder(ConfigurationSection section) {

        nameSpace = section.getString("namespace");
        id = section.getString("id");

        nameSpacedId = nameSpace + ":" + id;

    }

    public ItemStack build() {
        if (nameSpace == null || id == null) {
            throw new RuntimeException("NameSpace 和 ID为空，构建物品失败");
        } else {
            CustomStack customStack = CustomStack.getInstance(nameSpacedId);
            return customStack.getItemStack();
        }
    }
}
