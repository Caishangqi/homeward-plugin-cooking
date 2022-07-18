package homeward.plugin.homewardcooking.pojo.cookingrecipe;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DictionaryLabel {

    private String dictionaryName;
    private List<RecipeContent> dictionaryIncluded = new ArrayList<>();

}
