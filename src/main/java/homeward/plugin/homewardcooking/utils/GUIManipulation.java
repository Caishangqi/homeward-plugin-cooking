package homeward.plugin.homewardcooking.utils;

import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.CommonMaterial;
import homeward.plugin.homewardcooking.pojo.CookingData;

import java.io.IOException;
import java.util.Set;
import java.util.logging.Level;

public class GUIManipulation {

    private GUIManipulation() {
        throw new java.lang.UnsupportedOperationException("这是一个工具类，不能被实例化");
    }

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
                if (HomewardCooking.configurationLoader.isDebugMode())
                    CommonUtils.log(Level.ALL, Type.LOG, "拖拽物品影响下方物品栏: &6" + cache);
                return cache;
            }


        }
        return cache;
    }

    public static void dataInjectionToGUI(CookingData cookingData, CookingGUI cookingGUI) throws IOException, ClassNotFoundException {

        cookingData.getInputSlot().forEach((K, V) -> {
            if (cookingData.getInputSlot().get(K) == null) {
                cookingGUI.getInventory().setItem(K, CommonMaterial.AIR.getItemStack());
            } else {
                cookingGUI.getInventory().setItem(K, cookingData.getInputSlot().get(K));
            }

        });

        if (cookingData.getMainOutput() == null) {
            cookingGUI.getInventory().setItem(HomewardCooking.configurationLoader.getGUIOutputSlot(), CommonMaterial.AIR.getItemStack());
        } else {
            cookingGUI.getInventory().setItem(HomewardCooking.configurationLoader.getGUIOutputSlot(), cookingData.getMainOutput());
        }


    }
}
