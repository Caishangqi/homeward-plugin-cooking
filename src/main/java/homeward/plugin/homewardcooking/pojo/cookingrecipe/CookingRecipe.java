package homeward.plugin.homewardcooking.pojo.cookingrecipe;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CookingRecipe implements Serializable {

    @Serial
    private static final long serialVersionUID = 2291254551739175683L;
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
            ItemStack objectMaterial = recipes.getObjectMaterial();
            objectItems.add(objectMaterial);
        }
        return objectItems;
    }

}
