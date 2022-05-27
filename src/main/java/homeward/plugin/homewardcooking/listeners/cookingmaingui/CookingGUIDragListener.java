package homeward.plugin.homewardcooking.listeners.cookingmaingui;


import homeward.plugin.homewardcooking.guis.GUIImpl;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;


public class CookingGUIDragListener implements Listener {

    @EventHandler
    public void onCookingGUIDrag(InventoryDragEvent event) {
        InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (inventoryHolder instanceof GUIImpl) {
            event.setCancelled(true);
        }
    }

}
