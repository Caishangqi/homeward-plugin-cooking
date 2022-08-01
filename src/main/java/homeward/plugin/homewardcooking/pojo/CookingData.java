package homeward.plugin.homewardcooking.pojo;

import jline.internal.Nullable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CookingData implements Serializable {


    private String recipeKey;
    private String processObject;

    private String slotI;
    private String slotII;
    private String slotIII;
    private String slotIV;

    private byte[] makabaka;

    private String mainOutput;
    private String additionalOutput;

    private Integer timeRemaining;
    private Integer timeTotal;


}
