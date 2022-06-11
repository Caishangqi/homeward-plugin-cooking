package homeward.plugin.homewardcooking.pojo;

import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CookingPotThing {

    private ItemStack vanillaItemStack;

    public ItemStack getVanillaItemStack() {
        ItemStack cauldron = new ItemStack(Material.CAULDRON);
        NBTItem nbtCauldron = new NBTItem(new ItemStack(cauldron));
        nbtCauldron.setInteger("CookingPot",1);
        nbtCauldron.applyNBT(cauldron);

        ItemMeta cauldronItemMeta = cauldron.getItemMeta();
        cauldronItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f厨艺锅"));
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.translateAlternateColorCodes('&',"&7崭新的厨艺锅，放置在地上打开"));
        cauldronItemMeta.setLore(lore);
        cauldron.setItemMeta(cauldronItemMeta);

        vanillaItemStack = cauldron;

        return vanillaItemStack;

    }
}
