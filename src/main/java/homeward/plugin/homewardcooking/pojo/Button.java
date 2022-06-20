package homeward.plugin.homewardcooking.pojo;

import homeward.plugin.homewardcooking.utils.CommonUtils;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;


public enum Button {

    START_BUTTON(Material.OAK_BUTTON,"&6开始烹饪"),
    RECIPE_BUTTON(Material.KNOWLEDGE_BOOK,"&7游览已知配方")
    ;
    private ItemStack itemStack;
    private Integer customModelData;
    private Material material;
    private String name;

    Button(ItemStack itemStack, Integer customModelData) {
        this.itemStack = itemStack;
        this.customModelData = customModelData;
    }

    Button(Material material, String name) {
        this.material = material;
        this.name = name;
    }

    Button(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Integer getCustomModelData() {
        return customModelData;
    }

    public Material getMaterial() {
        return material;
    }

    public String getName() {
        return name;
    }

    public ItemStack getButton() {
        return CommonUtils.getInstance().buildItems(this.getMaterial(), this.getName());

    /**
        switch(this) {
            case START_BUTTON:
                return CommonUtils.getInstance().buildItems(START_BUTTON.getMaterial(), START_BUTTON.getName());
            case RECIPE_BUTTON:
                return CommonUtils.getInstance().buildItems(RECIPE_BUTTON.getMaterial(),RECIPE_BUTTON.getName());
        }
    */


    }

}
