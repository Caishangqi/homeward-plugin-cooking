package homeward.plugin.homewardcooking.listeners.cookingmaingui;


import homeward.plugin.homewardcooking.guis.CookingGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;


public class CookingGUIDragListener implements Listener {

    @EventHandler
    public void onCookingGUIDrag(InventoryDragEvent event) throws InterruptedException {

        InventoryHolder holder = event.getInventory().getHolder();

        InventoryHolder top = event.getView().getTopInventory().getHolder();
        InventoryHolder bottom = event.getView().getBottomInventory().getHolder();

        if (holder instanceof CookingGUI) {
            ((CookingGUI) holder).dragItem(event);
        }




    }

}
