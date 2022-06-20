package homeward.plugin.homewardcooking.listeners.cookingmaingui;

import de.tr7zw.changeme.nbtapi.NBTFile;
import homeward.plugin.homewardcooking.Homewardcooking;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.CookingData;
import homeward.plugin.homewardcooking.utils.StreamItemsUtils;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

public class CookingGUICloseListener implements Listener {
    @EventHandler
    public void onCookingGUIClose(InventoryCloseEvent event) throws IOException {
        //TODO GUI关闭逻辑
        if (event.getView().getTopInventory().getHolder() instanceof CookingGUI) {
            Player player = (Player) event.getPlayer();
            CookingGUI cookingGUI = (CookingGUI) event.getView().getTopInventory().getHolder();
            cookingGUI.removePlayerFromOpenPlayers(player);

            NBTFile file = new NBTFile(new File(event.getPlayer().getWorld().getWorldFolder().getName(), "cooking-data.nbt"));

            String locationKey = cookingGUI.getLocationKey();
            CookingData cookingData = file.getObject(locationKey, CookingData.class);

            int[] avaliableInputSlots = new int[]{38, 29, 20, 11};

            if (cookingGUI.getInventory().getItem(avaliableInputSlots[3]) != null) {
                cookingData.setSlotI(StreamItemsUtils.writeEncodedObject(cookingGUI.getInventory().getItem(avaliableInputSlots[3])));
            } else {
                cookingData.setSlotI(StreamItemsUtils.writeEncodedObject(new ItemStack(Material.AIR)));
            }

            if (cookingGUI.getInventory().getItem(avaliableInputSlots[2]) != null) {
                cookingData.setSlotII(StreamItemsUtils.writeEncodedObject(cookingGUI.getInventory().getItem(avaliableInputSlots[2])));
            } else {
                cookingData.setSlotII(StreamItemsUtils.writeEncodedObject(new ItemStack(Material.AIR)));
            }

            if (cookingGUI.getInventory().getItem(avaliableInputSlots[1]) != null) {
                cookingData.setSlotIII(StreamItemsUtils.writeEncodedObject(cookingGUI.getInventory().getItem(avaliableInputSlots[1])));
            } else {
                cookingData.setSlotIII(StreamItemsUtils.writeEncodedObject(new ItemStack(Material.AIR)));
            }

            if (cookingGUI.getInventory().getItem(avaliableInputSlots[0]) != null) {
                cookingData.setSlotIV(StreamItemsUtils.writeEncodedObject(cookingGUI.getInventory().getItem(avaliableInputSlots[0])));
            } else {
                cookingData.setSlotIV(StreamItemsUtils.writeEncodedObject(new ItemStack(Material.AIR)));
            }

            file.setObject(locationKey,cookingData);
            file.save();

            //如果这个GUI没有任何人查看，就直接把他删除否则一直存放到 GUIPoll中
            if (Homewardcooking.GUIPools.get(locationKey).getOpenedPlayers().size() == 0) {
                Homewardcooking.GUIPools.remove(locationKey);
                player.sendMessage("移除无人查看的GUI");
            }

        }
    }
}
