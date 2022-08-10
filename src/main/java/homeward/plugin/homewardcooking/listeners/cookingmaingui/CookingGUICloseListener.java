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
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CookingGUICloseListener implements Listener {
    @EventHandler
    public void onCookingGUIClose(InventoryCloseEvent event) throws IOException {
        //TODO GUI关闭逻辑
        if (event.getView().getTopInventory().getHolder() instanceof CookingGUI cookingGUI) {
            Player player = (Player) event.getPlayer();
            cookingGUI.removePlayerFromOpenPlayers(player);

            NBTFile file = new NBTFile(new File(event.getPlayer().getWorld().getWorldFolder().getName(), "cooking-data.nbt"));

            String locationKey = cookingGUI.getLocationKey();
            CookingData cookingData = (CookingData) StreamItemsUtils.deserializeBytes(file.getObject(locationKey, byte[].class));

            int[] avaliableInputSlots = HomewardCooking.configurationLoader.getGUIInputSlot();
            List<Integer> inputSlotIndex = Arrays.stream(avaliableInputSlots).boxed().toList();

            inputSlotIndex.forEach(K -> {
                if (cookingGUI.getInventory().getItem(K) != null) {
                    cookingData.getInputSlot().put(K, cookingGUI.getInventory().getItem(K));
                } else {
                    cookingData.getInputSlot().put(K, CommonMaterial.AIR.getItemStack());
                }
            });


            //TODO 这里摆烂了
            try {
                ItemStack itemStack = cookingGUI.getInventory().getItem(HomewardCooking.configurationLoader.getGUIOutputSlot());
                if (itemStack != null) {

                    cookingData.setMainOutput((itemStack));
                } else {
                    cookingData.setMainOutput(CommonMaterial.AIR.getItemStack());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }


            file.setObject(locationKey, StreamItemsUtils.serializeAsBytes(cookingData));
            file.save();

            //如果这个GUI没有任何人查看，就直接把他删除否则一直存放到 GUIPoll中
            if (HomewardCooking.GUIPools.get(locationKey).getOpenedPlayers().size() == 0) {
                HomewardCooking.GUIPools.remove(locationKey);
                if (HomewardCooking.configurationLoader.isDebugMode())
                    player.sendMessage("移除无人查看的GUI");
            }

        }
    }
}
