package homeward.plugin.homewardcooking.pojo.cookingrecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CookingRecipe {

    private List<RecipeContent> contents = new ArrayList<>();
    private RecipeContent mainOutPut;
    private List<RecipeContent> additionalOutPut = new ArrayList<>();
    private String recipeName;

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
}
