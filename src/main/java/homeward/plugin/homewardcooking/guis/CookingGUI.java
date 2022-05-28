package homeward.plugin.homewardcooking.guis;

import homeward.plugin.homewardcooking.utils.GUIManipulation;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

import static java.lang.Thread.sleep;
import static org.bukkit.event.inventory.InventoryAction.*;

public class CookingGUI extends GUI {

    private String guiName;

    @Override
    public String getGuiName() {
        return guiName;
    }

    @Override
    public void setGuiName(String guiName) {
        this.guiName = guiName;
    }

    @Override
    public int getSlot() {
        return 54;
    }

    @Override
    public void handelMenu(InventoryClickEvent e) {

        if (e.getAction() == MOVE_TO_OTHER_INVENTORY) {
            System.out.println("move out");
            e.setCancelled(true);
        }
    }

    @Override
    public void dragItem(InventoryDragEvent e) throws InterruptedException {

        System.out.println(e.getRawSlots());
        e.setCancelled(true);
        //如果是上方则取消drag
        if (!GUIManipulation.isDragOnTop(e.getRawSlots(), e.getView().getTopInventory().getSize())) {
            e.setCancelled(false);
        }


    }

    @Override
    public void moveItem(InventoryMoveItemEvent e) {

    }

    @Override
    public void setMenuItems() {

    }
}
