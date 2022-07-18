package homeward.plugin.homewardcooking.pojo.cookingrecipe;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CookingRecipe implements Serializable {

    private List<RecipeContent> contents = new ArrayList<>();
    private RecipeContent mainOutPut;
    private List<RecipeContent> additionalOutPut = new ArrayList<>();
    private String recipeName;
    //一个配方应该对应这一个值 配置文件的顶部
    private String recipeKey;
    private Integer totalRequiredTimes = 0;

    @Getter(AccessLevel.NONE)
    private List<ItemStack> objectItems = new ArrayList<>();

    public List<ItemStack> getObjectItems() {

        for (RecipeContent recipes : contents) {
            ItemStack objectMaterial = (ItemStack) recipes.getObjectMaterial();
            objectItems.add(objectMaterial);
        }
        return objectItems;
    }

}
