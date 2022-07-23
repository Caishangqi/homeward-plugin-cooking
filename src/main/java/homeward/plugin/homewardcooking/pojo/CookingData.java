package homeward.plugin.homewardcooking.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
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


}
