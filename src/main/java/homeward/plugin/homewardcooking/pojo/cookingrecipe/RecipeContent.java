package homeward.plugin.homewardcooking.pojo.cookingrecipe;

import homeward.plugin.homewardcooking.utils.CommonUtils;
import homeward.plugin.homewardcooking.utils.Type;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.logging.Level;

public class RecipeContent implements Serializable {

    private Integer timeRequired;
    private String type;
    private String material;
    private String command;
    //真正序列化完毕的物品在这里
    private Object objectMaterial;

    private Integer quantity = 1;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Object getObjectMaterial() {
        return objectMaterial;
    }

    public void setObjectMaterial(Object objectMaterial) {
        this.objectMaterial = objectMaterial;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

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

    public boolean setMaterial(String material, String key) {

        this.material = material;


        if (this.type != null) {
            switch (getType()) {
                case "minecraft":
                    try {
                        ItemStack objectMaterial = new ItemStack(Material.valueOf(material), quantity);
                        this.setObjectMaterial(objectMaterial);
                    } catch (Exception exception) {
                        CommonUtils.getInstance().log(Level.WARNING, Type.UNLOADED, "未找到以开头 " + this.type + " 类型的 " + material + " 物品");
                        exception.printStackTrace();
                    }

                    break;
                case "MMOITEMS":
                    CommonUtils.getInstance().log(Level.WARNING, Type.UNLOADED, "暂不支持的物品类型，尽情期待");
            }
        } else {
            CommonUtils.getInstance().log(Level.WARNING, Type.UNLOADED, "配方 " + key + "中没有找到相应的 type 元素(type应当写在前)");
            return true;
        }
        return true;
    }
}
