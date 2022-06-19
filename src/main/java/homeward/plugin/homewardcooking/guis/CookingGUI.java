package homeward.plugin.homewardcooking.guis;

import homeward.plugin.homewardcooking.utils.GUIManipulation;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Thread.sleep;
import static org.bukkit.event.inventory.InventoryAction.*;

public class CookingGUI extends GUI {

    private String guiName;
    private static final int[] avaliableInputSlots = new int[]{38, 29, 20, 11};
    private static final int avaliableOuputSlots = 24;
    private static final int miscellaneousSlots = 42;
    private static final int startButton = 40;
    private static final int recipesButton = 13;

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

        if (e.getRawSlot() == 40) {
            e.setCancelled(true);
        }

        if (Arrays.stream(avaliableInputSlots).boxed().collect(Collectors.toList()).contains(e.getRawSlot())) {
            e.setCancelled(false);
        } else {
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

        ItemStack button = new ItemStack(Material.OAK_BUTTON);
        ItemMeta buttonItemMeta = button.getItemMeta();
        buttonItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6开始烹饪"));
        button.setItemMeta(buttonItemMeta);

        ItemStack recipesButton = new ItemStack(Material.KNOWLEDGE_BOOK);
        ItemMeta recipesButtonItemMeta = recipesButton.getItemMeta();
        recipesButtonItemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&7游览已知配方"));
        recipesButton.setItemMeta(recipesButtonItemMeta);


        inventory.setItem(startButton, button);
        inventory.setItem(this.recipesButton, recipesButton);

        fillMenu();

    }

    private void fillMenu() {
        List<Integer> list = Arrays.stream(avaliableInputSlots).boxed().collect(Collectors.toList());
        list.add(avaliableOuputSlots);
        list.add(miscellaneousSlots);
        list.add(startButton);
        list.add(recipesButton);

        ItemStack fillBlock = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        ItemMeta fillBlockItemMeta = fillBlock.getItemMeta();
        fillBlockItemMeta.setDisplayName(" ");
        fillBlock.setItemMeta(fillBlockItemMeta);

        for (int i = 0; i <= getSlot() - 1; i++) {
            if (list.contains(i)) {

            } else {
                inventory.setItem(i, fillBlock);
            }
        }
    }
}
