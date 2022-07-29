package homeward.plugin.homewardcooking.pojo.cookingrecipe;

import homeward.plugin.homewardcooking.utils.CommonUtils;
import homeward.plugin.homewardcooking.utils.Type;
import io.lumine.mythic.lib.api.item.NBTItem;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.Serializable;
import java.util.logging.Level;

@Getter
@Setter
public class RecipeContent implements Serializable {

    private Integer timeRequired;
    private String type;
    @Setter(AccessLevel.NONE)
    private String material;
    private String command;
    //真正序列化完毕的物品在这里
    private Object objectMaterial;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private Integer quantity = 1;

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;

        //如果顺序是先建立object物品那么在这里检查，如果以及有object直接把这个object
        //转换成ItemStack然后赐予数量
        if (objectMaterial != null) {
            ItemStack itemStack = (ItemStack) objectMaterial;
            itemStack.setAmount(quantity);
            this.objectMaterial = itemStack;
        }

        setMaterial(material, type);
    }

    //Unstable

    public Integer getQuantity() {
        ItemStack obj = (ItemStack) objectMaterial;
        return obj.getAmount();
    }

    public boolean setMaterial(String material, String key) {

        ItemStack obj = (ItemStack) this.objectMaterial;

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
            if (NBTItem.get(obj).getType() == null) {
                CommonUtils.getInstance().log(Level.WARNING, Type.UNLOADED, "配方 " + key + "中没有找到相应的 type 元素(type应当写在前)");
            }
            return true;
        }
        return true;
    }
}
