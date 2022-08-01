package homeward.plugin.homewardcooking.pojo;

import jline.internal.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


public class CookingData implements Serializable {


    private String recipeKey;
    private CookingProcessObject processObject;

    private LinkedHashMap<Integer, ItemStack> inputSlot = new LinkedHashMap<>();
    private ItemStack slotI;
    private ItemStack slotII;
    private ItemStack slotIII;
    private ItemStack slotIV;

    private ItemStack mainOutput;
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

    public ItemStack getSlotI() {
        return slotI;
    }

    public void setSlotI(ItemStack slotI) {
        this.slotI = slotI;
    }

    public ItemStack getSlotII() {
        return slotII;
    }

    public void setSlotII(ItemStack slotII) {
        this.slotII = slotII;
    }

    public ItemStack getSlotIII() {
        return slotIII;
    }

    public void setSlotIII(ItemStack slotIII) {
        this.slotIII = slotIII;
    }

    public ItemStack getSlotIV() {
        return slotIV;
    }

    public void setSlotIV(ItemStack slotIV) {
        this.slotIV = slotIV;
    }

    public ItemStack getMainOutput() {
        return mainOutput;
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
