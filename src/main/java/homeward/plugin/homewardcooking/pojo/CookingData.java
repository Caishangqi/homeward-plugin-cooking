package homeward.plugin.homewardcooking.pojo;

import java.io.Serializable;

public class CookingData implements Serializable {

    private String recipeKey;
    private String processObject;

    private String slotI;
    private String slotII;
    private String slotIII;
    private String slotIV;

    private String mainOutput;
    private String additionalOutput;

    private Integer timeRemaining;
    private Integer timeTotal;

    public String getProcessObject() {
        return processObject;
    }

    public void setProcessObject(String processObject) {
        this.processObject = processObject;
    }

    public String getRecipeKey() {
        return recipeKey;
    }

    public void setRecipeKey(String recipe) {
        this.recipeKey = recipeKey;
    }

    public String getSlotI() {
        return slotI;
    }

    public void setSlotI(String slotI) {
        this.slotI = slotI;
    }

    public String getSlotII() {
        return slotII;
    }

    public void setSlotII(String slotII) {
        this.slotII = slotII;
    }

    public String getSlotIII() {
        return slotIII;
    }

    public void setSlotIII(String slotIII) {
        this.slotIII = slotIII;
    }

    public String getSlotIV() {
        return slotIV;
    }

    public void setSlotIV(String slotIV) {
        this.slotIV = slotIV;
    }

    public String getMainOutput() {
        return mainOutput;
    }

    public void setMainOutput(String mainOutput) {
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
