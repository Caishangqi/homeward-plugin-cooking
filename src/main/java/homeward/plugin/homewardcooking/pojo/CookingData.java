package homeward.plugin.homewardcooking.pojo;

import dev.lone.itemsadder.api.CustomStack;
import homeward.plugin.homewardcooking.utils.StreamItemsUtils;
import org.bukkit.inventory.ItemStack;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class CookingData implements Serializable {

    @Serial
    private static final long serialVersionUID = 2291254551739175681L;
    private String recipeKey;
    private CookingProcessObject processObject;
    private LinkedHashMap<Integer, ItemStack> inputSlot = new LinkedHashMap<>();
    private ItemStack mainOutput = CommonMaterial.AIR.getItemStack();
    private String additionalOutput;

    private Integer timeRemaining;
    private Integer timeTotal;

    public String getRecipeKey() {
        return recipeKey;
    }

    public LinkedHashMap<Integer, ItemStack> getInputSlot() {
        return inputSlot;
    }

    public void setInputSlot(LinkedHashMap<Integer, ItemStack> inputSlot) {
        this.inputSlot = inputSlot;
    }

    public void setRecipeKey(String recipeKey) {
        this.recipeKey = recipeKey;
    }

    public CookingProcessObject getProcessObject() {
        return processObject;
    }

    public void setProcessObject(CookingProcessObject processObject) {
        this.processObject = processObject;
    }


    public ItemStack getMainOutput() {
        if (!mainOutput.getType().isAir())
            return StreamItemsUtils.deserializeItem(StreamItemsUtils.serializeItem(mainOutput));
        else return mainOutput;
    }

    public void setMainOutput(ItemStack mainOutput) {
        this.mainOutput = mainOutput;
    }

    public String getAdditionalOutput() {
        return additionalOutput;
    }

    public void setAdditionalOutput(String additionalOutput) {
        this.additionalOutput = additionalOutput;
    }

    public Integer getTimeRemaining() {
        return timeRemaining;
    }

    public void setTimeRemaining(Integer timeRemaining) {
        this.timeRemaining = timeRemaining;
    }

    public Integer getTimeTotal() {
        return timeTotal;
    }

    public void setTimeTotal(Integer timeTotal) {
        this.timeTotal = timeTotal;
    }
}
