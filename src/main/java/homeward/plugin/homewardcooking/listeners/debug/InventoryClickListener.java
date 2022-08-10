package homeward.plugin.homewardcooking.listeners.debug;

import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import homeward.plugin.homewardcooking.utils.Type;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.logging.Level;

public class InventoryClickListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (HomewardCooking.configurationLoader.isDebugMode())
            CommonUtils.log(Level.ALL, Type.LOG, "所点击的物品栏原栏位为: &6" + event.getRawSlot());
    }
}
