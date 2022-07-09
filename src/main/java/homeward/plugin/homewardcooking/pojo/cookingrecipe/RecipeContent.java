package homeward.plugin.homewardcooking.pojo.cookingrecipe;

import org.bukkit.Material;

public class RecipeContent {

   private Integer timeRequired;
   private String type;
   private String material;

    public Integer getTimeRequired() {
        return timeRequired;
    }

    public void setTimeRequired(Integer timeRequired) {
        this.timeRequired = timeRequired;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }
}
