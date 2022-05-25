package homeward.plugin.homewardcooking.guis;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;

public interface GUI {



    String getGUIName();

    void setGUIName(String value);

    int getSlot();

    void handleClicked(InventoryClickEvent event);

    void handleDrag(InventoryDragEvent event);

    void handleMoved(InventoryMoveItemEvent event);

    void setGUIContent();

    void open(Player player);

    
}
