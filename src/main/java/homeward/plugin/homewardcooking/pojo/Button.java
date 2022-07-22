package homeward.plugin.homewardcooking.pojo;

import homeward.plugin.homewardcooking.utils.CommonUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;


public enum Button {

    START_BUTTON(Material.OAK_BUTTON, "&6开始烹饪", 40),
    RECIPE_BUTTON(Material.KNOWLEDGE_BOOK, "&7游览已知配方"),
    PROCESS_BUTTON(Material.ORANGE_STAINED_GLASS_PANE, "&7正在烹饪: "),
    READY_BUTTON(Material.GREEN_STAINED_GLASS_PANE, "&7就绪中..."),

    FILLED_BUTTON(Material.GRAY_STAINED_GLASS_PANE, "");

    private ItemStack itemStack;
    private Integer customModelData;
    private Material material;
    private String name;
    private Integer slot;

    Button(ItemStack itemStack, Integer customModelData) {
        this.itemStack = itemStack;
        this.customModelData = customModelData;
    }

    Button(Material material, String name) {
        this.material = material;
        this.name = name;
    }

    Button(Material material, String name, Integer slot) {
        this.material = material;
        this.name = name;
        this.slot = slot;
    }

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;

    }

    public void setMaterial(Material material) {
        this.material = material;
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
