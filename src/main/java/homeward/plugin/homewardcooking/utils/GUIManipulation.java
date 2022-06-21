package homeward.plugin.homewardcooking.utils;

import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.CookingData;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.Set;

public class GUIManipulation {

    /**
     * drag行为是否在上方GUI内
     *
     * @param dragSlot 传入玩家Drag的目标格数整数
     * @param size     当前GUI大小
     * @return drag行为是否在上方GUI内
     */
    public static boolean isDragOnTop(Set<Integer> dragSlot, int size) {
        boolean cache = false;
        for (Integer number : dragSlot) {

            //这块只能小于上方自定义gui的格数而不能大于它，无解
            //if (number> size -1) 这么写会产生两个GUI之间drag物品残留问题

            if (number <= size - 1) {
                cache = true;
                System.out.println("下面的" + cache);
                return cache;
            }


        }
        return cache;
    }

    public static void dataInjectionToGUI(CookingData cookingData, CookingGUI cookingGUI) throws IOException, ClassNotFoundException {

        if (cookingData.getSlotI() == null) {
            cookingGUI.getInventory().setItem(11, new ItemStack(Material.AIR));
        } else {
            cookingGUI.getInventory().setItem(11, (ItemStack) StreamItemsUtils.writeDecodedObject(cookingData.getSlotI()));
        }

        if (cookingData.getSlotII() == null) {
            cookingGUI.getInventory().setItem(20, new ItemStack(Material.AIR));
        } else {
            cookingGUI.getInventory().setItem(20, (ItemStack) StreamItemsUtils.writeDecodedObject(cookingData.getSlotII()));

        }

        if (cookingData.getSlotIII() == null) {
            cookingGUI.getInventory().setItem(29, new ItemStack(Material.AIR));
        } else {
            cookingGUI.getInventory().setItem(29, (ItemStack) StreamItemsUtils.writeDecodedObject(cookingData.getSlotIII()));

        }

        if (cookingData.getSlotIV() == null) {
            cookingGUI.getInventory().setItem(38, new ItemStack(Material.AIR));
        } else {
            cookingGUI.getInventory().setItem(38, (ItemStack) StreamItemsUtils.writeDecodedObject(cookingData.getSlotIV()));

        }


    }
}
