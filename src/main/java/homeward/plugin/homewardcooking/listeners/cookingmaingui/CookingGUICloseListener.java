package homeward.plugin.homewardcooking.listeners.cookingmaingui;

import de.tr7zw.changeme.nbtapi.NBTFile;
import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.CommonMaterial;
import homeward.plugin.homewardcooking.pojo.CookingData;
import homeward.plugin.homewardcooking.utils.StreamItemsUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

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

            int[] avaliableInputSlots = HomewardCooking.configurationLoader.getGUIInputSlot();

            if (cookingGUI.getInventory().getItem(avaliableInputSlots[3]) != null) {
                cookingData.setSlotI(StreamItemsUtils.writeEncodedObject(cookingGUI.getInventory().getItem(avaliableInputSlots[3])));
            } else {
                cookingData.setSlotI(StreamItemsUtils.writeEncodedObject(CommonMaterial.AIR.getItemStack()));
            }

            if (cookingGUI.getInventory().getItem(avaliableInputSlots[2]) != null) {
                cookingData.setSlotII(StreamItemsUtils.writeEncodedObject(cookingGUI.getInventory().getItem(avaliableInputSlots[2])));
            } else {
                cookingData.setSlotII(StreamItemsUtils.writeEncodedObject(CommonMaterial.AIR.getItemStack()));
            }

            if (cookingGUI.getInventory().getItem(avaliableInputSlots[1]) != null) {
                cookingData.setSlotIII(StreamItemsUtils.writeEncodedObject(cookingGUI.getInventory().getItem(avaliableInputSlots[1])));
            } else {
                cookingData.setSlotIII(StreamItemsUtils.writeEncodedObject(CommonMaterial.AIR.getItemStack()));
            }

            if (cookingGUI.getInventory().getItem(avaliableInputSlots[0]) != null) {
                cookingData.setSlotIV(StreamItemsUtils.writeEncodedObject(cookingGUI.getInventory().getItem(avaliableInputSlots[0])));
            } else {
                cookingData.setSlotIV(StreamItemsUtils.writeEncodedObject(CommonMaterial.AIR.getItemStack()));
            }

            //TODO 这里摆烂了
            try {
                if (cookingGUI.getInventory().getItem(HomewardCooking.configurationLoader.getGUIOutputSlot()) != null) {
                    cookingData.setMainOutput(StreamItemsUtils.writeEncodedObject(cookingGUI.getInventory().getItem(HomewardCooking.configurationLoader.getGUIOutputSlot())));
                } else {
                    cookingData.setMainOutput(StreamItemsUtils.writeEncodedObject(CommonMaterial.AIR.getItemStack()));
                }
            } catch (Exception exception) {

            }


            file.setObject(locationKey, cookingData);
            file.save();

            //如果这个GUI没有任何人查看，就直接把他删除否则一直存放到 GUIPoll中
            if (HomewardCooking.GUIPools.get(locationKey).getOpenedPlayers().size() == 0) {
                HomewardCooking.GUIPools.remove(locationKey);
                player.sendMessage("移除无人查看的GUI");
            }

        }
    }
}
