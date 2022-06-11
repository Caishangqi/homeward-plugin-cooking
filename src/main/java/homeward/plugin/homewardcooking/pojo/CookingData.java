package homeward.plugin.homewardcooking.pojo;

import org.bukkit.inventory.ItemStack;

public class CookingData {

    private String recipe;

    private ItemStack slotI;
    private ItemStack slotII;
    private ItemStack slotIII;
    private ItemStack slotIV;

    private ItemStack mainOutput;
    private ItemStack additionalOutput;

    private Integer timeRemaining;
    private Integer timeTotal;

    public String getRecipe() {
        return recipe;
    }

    public void setRecipe(String recipe) {
        this.recipe = recipe;
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

    public ItemStack getAdditionalOutput() {
        return additionalOutput;
    }

    public void setAdditionalOutput(ItemStack additionalOutput) {
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
