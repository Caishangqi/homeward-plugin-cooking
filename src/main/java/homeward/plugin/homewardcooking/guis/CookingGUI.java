package homeward.plugin.homewardcooking.guis;

import homeward.plugin.homewardcooking.events.CookingInitialEvent;
import homeward.plugin.homewardcooking.pojo.Button;
import homeward.plugin.homewardcooking.pojo.CommonMaterial;
import homeward.plugin.homewardcooking.utils.GUIManipulation;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.bukkit.event.inventory.InventoryAction.MOVE_TO_OTHER_INVENTORY;

public class CookingGUI extends GUI {

    private String guiName;
    private String locationKey;
    private static final int[] avaliableInputSlots = new int[]{38, 29, 20, 11};
    private static final int avaliableOuputSlots = 24;
    private static final int miscellaneousSlots = 42;
    private static final int startButton = 40;
    private static final int recipesButton = 13;
    //谁打开了这个GUI
    private List<Player> openedPlayers = new ArrayList<>();

    public String getLocationKey() {
        return locationKey;
    }

    public void setLocationKey(String locationKey) {
        this.locationKey = locationKey;
    }

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

    public List<Player> getOpenedPlayers() {
        return openedPlayers;
    }

    public void setOpenedPlayers(List<Player> openedPlayers) {
        this.openedPlayers = openedPlayers;
    }

    public void addPlayerToOpenPlayers(Player player) {
        this.openedPlayers.add(player);
    }

    public void removePlayerFromOpenPlayers(Player player) {
        this.openedPlayers.remove(player);
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

        if (e.getRawSlot() == 24 && e.getCursor().getType() == Material.AIR) {
            e.setCancelled(false);
        }

        //配方按钮点击开始逻辑
        if (e.getRawSlot() == Button.START_BUTTON.getSlot()) {

            Player whoClicked = (Player) e.getWhoClicked();

            Bukkit.getServer().getPluginManager().callEvent(new CookingInitialEvent(whoClicked, getContainItemList(), locationKey));
            //whoClicked.playSound(whoClicked.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);

        }


    }

    public List<ItemStack> getContainItemList() {
        List<ItemStack> containedList = new ArrayList<>();
        for (int i : avaliableInputSlots) {
            try {
                containedList.add(this.getInventory().getItem(i));
            } catch (Exception exception) {
                containedList.add(CommonMaterial.AIR.getItemStack());
                exception.printStackTrace();
            }

        }

        return containedList;
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

        inventory.setItem(startButton, Button.START_BUTTON.getButton());
        inventory.setItem(recipesButton, Button.RECIPE_BUTTON.getButton());

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
