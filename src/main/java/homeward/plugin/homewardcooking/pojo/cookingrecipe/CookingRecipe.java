package homeward.plugin.homewardcooking.pojo.cookingrecipe;

import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CookingRecipe implements Serializable {

    private List<RecipeContent> contents = new ArrayList<>();
    private RecipeContent mainOutPut;
    private List<RecipeContent> additionalOutPut = new ArrayList<>();
    private String recipeName;
    //一个配方应该对应这一个值 配置文件的顶部
    private String recipeKey;
    private Integer totalRequiredTimes = 0;

    private List<ItemStack> objectItems = new ArrayList<>();

    public List<RecipeContent> getContents() {
        return contents;
    }

    public void setContents(List<RecipeContent> contents) {
        this.contents = contents;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public RecipeContent getMainOutPut() {
        return mainOutPut;
    }

    public void setMainOutPut(RecipeContent mainOutPut) {
        this.mainOutPut = mainOutPut;
    }

    public List<RecipeContent> getAdditionalOutPut() {
        return additionalOutPut;
    }

    public void setAdditionalOutPut(List<RecipeContent> additionalOutPut) {
        this.additionalOutPut = additionalOutPut;
    }

    public Integer getTotalRequiredTimes() {
        return totalRequiredTimes;
    }

    public void setTotalRequiredTimes(Integer totalRequiredTimes) {
        this.totalRequiredTimes = totalRequiredTimes;
    }

    public List<ItemStack> getObjectItems() {

        for (RecipeContent recipes : contents) {
            ItemStack objectMaterial = (ItemStack) recipes.getObjectMaterial();
            objectItems.add(objectMaterial);
        }
        return objectItems;
    }

    public void setObjectItems(List<ItemStack> objectItems) {
        this.objectItems = objectItems;
    }

    public String getRecipeKey() {
        return recipeKey;
    }

    public void setRecipeKey(String recipeKey) {
        this.recipeKey = recipeKey;
    }
}
