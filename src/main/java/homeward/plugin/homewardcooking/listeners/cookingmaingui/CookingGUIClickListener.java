package homeward.plugin.homewardcooking.listeners.cookingmaingui;

import homeward.plugin.homewardcooking.guis.CookingGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class CookingGUIClickListener implements Listener {

    @EventHandler
    public void onCookingGUIClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory() == null)
            return;

        InventoryHolder holder = event.getClickedInventory().getHolder();

        InventoryHolder top = event.getView().getTopInventory().getHolder();
        InventoryHolder bottom = event.getView().getBottomInventory().getHolder();

        if (event.getClick().isShiftClick() && top instanceof CookingGUI) {
            event.setCancelled(true);
            return;
        }

        if (holder instanceof CookingGUI) {
            ((CookingGUI) holder).handelMenu(event);
        }

    }
}
