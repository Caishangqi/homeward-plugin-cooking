package homeward.plugin.homewardcooking.guis;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.List;


public abstract class GUI implements InventoryHolder {

    protected String value;

    protected String guiName;

    protected List<Integer> doWorkSlot;

    protected Inventory inventory;

    protected Player whoOwnsInventory;


    public abstract String getGuiName();

    public abstract void setGuiName(String menuName);

    public abstract int getSlot();

    public abstract void handelMenu(InventoryClickEvent e);

    public abstract void dragItem(InventoryDragEvent e) throws InterruptedException;

    public abstract void moveItem(InventoryMoveItemEvent e);

    public abstract void setMenuItems();

    public void open(Player player) {

        if (whoOwnsInventory == null) {
            inventory = Bukkit.createInventory(this, getSlot(), getGuiName());
            this.setMenuItems();
            whoOwnsInventory = player;
            player.openInventory(inventory);
        } else {
            player.openInventory(inventory);
        }

    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
