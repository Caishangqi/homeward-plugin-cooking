package homeward.plugin.homewardcooking.pojo;

import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
import org.bukkit.entity.Player;

public class CookingProcessObject {

    private CookingRecipe cookingRecipe;
    private Player whoStart;
    private Integer remainTime;
    private Integer totalTime;

    public CookingRecipe getCookingRecipe() {
        return cookingRecipe;
    }

    public void setCookingRecipe(CookingRecipe cookingRecipe) {
        this.cookingRecipe = cookingRecipe;
    }

    public Player getWhoStart() {
        return whoStart;
    }

    public void setWhoStart(Player whoStart) {
        this.whoStart = whoStart;
    }

    public Integer getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(Integer remainTime) {
        this.remainTime = remainTime;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }
}
