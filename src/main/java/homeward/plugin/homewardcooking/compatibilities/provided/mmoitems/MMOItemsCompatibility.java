package homeward.plugin.homewardcooking.compatibilities.provided.mmoitems;

import homeward.plugin.homewardcooking.compatibilities.CompatibilityPlugin;
import io.lumine.mythic.lib.api.item.NBTItem;
import net.Indyuce.mmoitems.MMOItems;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class MMOItemsCompatibility<Plugin> extends CompatibilityPlugin<MMOItems> {

    public static boolean isSimilar(ItemStack firstItems, ItemStack secondItems) {

        NBTItem nbtItemOfItem1 = NBTItem.get(firstItems);
        NBTItem nbtItemOfItem2 = NBTItem.get(secondItems);

        if (nbtItemOfItem1.getType() == null || nbtItemOfItem2.getType() == null) {
            return false;
        } else if (Objects.equals(nbtItemOfItem1.getType(), nbtItemOfItem2.getType())) {
            return Objects.equals(nbtItemOfItem1.getString("MMOITEMS_ITEM_ID"), nbtItemOfItem2.getString("MMOITEMS_ITEM_ID"));
        }

        return false;
    }

}
