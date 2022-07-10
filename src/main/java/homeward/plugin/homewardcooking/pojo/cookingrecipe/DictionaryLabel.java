package homeward.plugin.homewardcooking.pojo.cookingrecipe;

import java.util.ArrayList;
import java.util.List;

public class DictionaryLabel {

    private String dictionaryName;
    private List<RecipeContent> dictionaryIncluded = new ArrayList<>();

    public String getDictionaryName() {
        return dictionaryName;
    }

    public void setDictionaryName(String dictionaryName) {
        this.dictionaryName = dictionaryName;
    }

    public List<RecipeContent> getDictionaryIncluded() {
        return dictionaryIncluded;
    }

    public void setDictionaryIncluded(List<RecipeContent> dictionaryIncluded) {
        this.dictionaryIncluded = dictionaryIncluded;
    }
}
