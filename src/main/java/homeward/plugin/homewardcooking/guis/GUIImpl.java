package homeward.plugin.homewardcooking.guis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
/**
 * Weak Access
 */
public class GUIImpl implements GUI, InventoryHolder {

    public String value = null;

    public String guiname = null;

    public Inventory inventory = null;

    public int size = 9;

    @Override
    public String getGUIName() {
        return guiname;
    }

    @Override
    public void setGUIName(String value) {
        guiname = value;
    }

    @Override
    public int getSlot() {
        return 0;
    }

    @Override
    public void handleClicked(InventoryClickEvent event) {

    }

    @Override
    public void handleDrag(InventoryDragEvent event) {
        /**/
    }

    @Override
    public void handleMoved(InventoryMoveItemEvent event) {
        /**/
    }

    @Override
    public void setGUIContent() {

    }

    @Override
    public void open(Player player) {
        Inventory inventory = Bukkit.createInventory(player, size);

    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
