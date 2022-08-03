package homeward.plugin.homewardcooking.pojo;

import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class CookingProcessObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 2291254551739175682L;

    private CookingRecipe cookingRecipe;
    private Player whoStart;
    private Integer remainTime;
    private Integer totalTime;

}
