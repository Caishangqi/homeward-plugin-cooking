package homeward.plugin.homewardcooking.compatibilities.provided.itemsadder;

import de.tr7zw.changeme.nbtapi.NBTFile;
import de.tr7zw.changeme.nbtapi.NBTItem;
import dev.lone.itemsadder.api.CustomBlock;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import dev.lone.itemsadder.api.Events.CustomBlockInteractEvent;
import dev.lone.itemsadder.api.Events.CustomBlockPlaceEvent;
import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import dev.lone.itemsadder.api.ItemsAdder;
import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.compatibilities.CompatibilityPlugin;
import homeward.plugin.homewardcooking.events.GUIOpenEvent;
import homeward.plugin.homewardcooking.pojo.CookingData;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.RecipeContent;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import homeward.plugin.homewardcooking.utils.StreamItemsUtils;
import homeward.plugin.homewardcooking.utils.Type;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;
import static org.bukkit.inventory.EquipmentSlot.HAND;

public class ItemsAdderCompatibility extends CompatibilityPlugin<ItemsAdder> {

    public static boolean isSimilar(ItemStack firstItems, ItemStack secondItems) {

        if (CustomStack.byItemStack(firstItems) == null || CustomStack.byItemStack(secondItems) == null) {
            return false;
        } else {

            //先判断是不是同一个插件的物品
            NBTItem nbtItem1 = new NBTItem(firstItems);
            NBTItem nbtItem2 = new NBTItem(secondItems);

            String id1 = nbtItem1.getCompound("itemsadder").getString("id");
            String namespace1 = nbtItem1.getCompound("itemsadder").getString("namespace");
            String nameSpaceAndId1 = namespace1 + ":" + id1;

            String id2 = nbtItem2.getCompound("itemsadder").getString("id");
            String namespace2 = nbtItem2.getCompound("itemsadder").getString("namespace");
            String nameSpaceAndId2 = namespace2 + ":" + id2;

            return Objects.equals(nameSpaceAndId1, nameSpaceAndId2);

        }
    }

    public static void saveItemAdderCompatibility(LinkedHashMap<Integer, ItemStack> itemStackList) {

        itemStackList.forEach((K, V) -> {
            if (!itemStackList.get(K).getType().isAir())
                itemStackList.put(K, StreamItemsUtils.deserializeItem(StreamItemsUtils.serializeItem(itemStackList.get(K))));

        });

    }

    public static void saveItemAdderCompatibility(List<RecipeContent> recipeContentList) {
        recipeContentList.forEach(K -> {
            if (!K.getObjectMaterial().getType().isAir()) {
                K.setObjectMaterial(StreamItemsUtils.deserializeItem(StreamItemsUtils.serializeItem(K.getObjectMaterial())));
            }

        });
    }

    private boolean doUseCustomBlock() {
        //这个默认是False
        return HomewardCooking.configurationLoader.getGeneralSettings().getBoolean("use-custom-blocks", false);
    }

    @EventHandler(ignoreCancelled = true)
    public void onItemsAdderLoadData(ItemsAdderLoadDataEvent event) {
        HomewardCooking.recipesLoader.importRecipes();
        CommonUtils.log(Level.INFO, Type.LOADED, "配方加载成功 (顺序改变因为 &6ItemsAdder&7)");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onCustomBlockBreak(CustomBlockBreakEvent event) {
        System.out.println("我打碎了一个自定义方块?");
        if (doUseCustomBlock()) {

            Player player = event.getPlayer();
            String customBlockNameSpace = HomewardCooking.configurationLoader.getGeneralSettings().getString("custom-blocks-namespace");
            String namespacedID = event.getNamespacedID();

            if (Objects.equals(customBlockNameSpace, namespacedID)) {
                NBTFile file = null;
                try {
                    file = new NBTFile(new File(event.getPlayer().getWorld().getWorldFolder().getName(), "cooking-data.nbt"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                Block breakBlock = event.getBlock();

                int blockX = breakBlock.getLocation().getBlockX();
                int blockY = breakBlock.getLocation().getBlockY();
                int blockZ = breakBlock.getLocation().getBlockZ();

                String locationKey = breakBlock.getWorld() + " " + blockX + " " + blockY + " " + blockZ;

                if (file.hasKey(locationKey)) {

                    //如果其他人打碎Pot那么需要关闭打开这个Pot所有玩家的GUI防止刷物品
                    if (HomewardCooking.GUIPools.containsKey(locationKey)) {
                        List<Player> openedPlayers = HomewardCooking.GUIPools.get(locationKey).getOpenedPlayers();
                        openedPlayers.forEach(HumanEntity::closeInventory);
                    }

                    //爆物品
                    CookingData cookingData = (CookingData) StreamItemsUtils.deserializeBytes(file.getObject(locationKey, byte[].class));
                    List<ItemStack> containedItemsInData = CommonUtils.getContainedItemsInData(cookingData);

                    file.removeKey(locationKey);
                    try {
                        file.save();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    player.sendMessage("移除物品数据成功");
                    //聪明代码
                    //Bukkit.getServer().getWorld(event.getPlayer().getWorld().getName()).dropItem(breakBlock.getLocation(), CookingPotThing.getVanillaItemStack());
                    containedItemsInData.forEach(K -> {
                        if (K != null)
                            Bukkit.getServer().getWorld(event.getPlayer().getWorld().getName()).dropItem(breakBlock.getLocation(), K);
                    });

                }
            }

        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) throws IOException {

        if (doUseCustomBlock()) {
            NBTFile file = null;
            file = new NBTFile(new File(event.getPlayer().getWorld().getWorldFolder().getName(), "cooking-data.nbt"));

            Location blockLocation = event.getBlock().getLocation();
            Player player = event.getPlayer();
            String toStringBlockLocationKey = CommonUtils.toStringBlockLocationKey(blockLocation);

            if (file.hasKey(toStringBlockLocationKey)) {


                //如果其他人打碎Pot那么需要关闭打开这个Pot所有玩家的GUI防止刷物品
                if (HomewardCooking.GUIPools.containsKey(toStringBlockLocationKey)) {
                    List<Player> openedPlayers = HomewardCooking.GUIPools.get(toStringBlockLocationKey).getOpenedPlayers();
                    openedPlayers.forEach(HumanEntity::closeInventory);
                }

                //爆物品
                CookingData cookingData = (CookingData) StreamItemsUtils.deserializeBytes(file.getObject(toStringBlockLocationKey, byte[].class));
                List<ItemStack> containedItemsInData = CommonUtils.getContainedItemsInData(cookingData);

                event.setDropItems(false);
                file.removeKey(toStringBlockLocationKey);
                file.save();
                player.sendMessage("移除物品数据成功");
                //聪明代码 //CookingPotThing.getVanillaItemStack() CustomBlock.byAlreadyPlaced(event.getBlock());
                Bukkit.getServer().getWorld(event.getPlayer().getWorld().getName()).dropItem(blockLocation, CustomBlock.byAlreadyPlaced(event.getBlock()).getItemStack());
                containedItemsInData.forEach(K -> {
                    if (K != null)
                        Bukkit.getServer().getWorld(event.getPlayer().getWorld().getName()).dropItem(blockLocation, K);
                });


            }
        }


    }

    @EventHandler
    public void onCustomBlockInteract(CustomBlockInteractEvent event) throws IOException {
        if (doUseCustomBlock()) {
            Player player = event.getPlayer();
            Action action = event.getAction();
            String customBlockNameSpace = HomewardCooking.configurationLoader.getGeneralSettings().getString("custom-blocks-namespace");
            String namespacedID = event.getNamespacedID();

            if (Objects.equals(namespacedID, customBlockNameSpace) && action == RIGHT_CLICK_BLOCK) {
                Block clickedBlock = event.getBlockClicked();

                String toStringBlockLocationKey = CommonUtils.toStringBlockLocationKey(clickedBlock.getLocation());

                NBTFile file = new NBTFile(new File(event.getPlayer().getWorld().getWorldFolder().getName(), "cooking-data.nbt"));

                //PlayerInteractEvent默认主手副手都触发 除非判断主手
                if (file.hasKey(toStringBlockLocationKey) && event.getHand() == HAND) {
                    Bukkit.getServer().getPluginManager().callEvent(new GUIOpenEvent(player, toStringBlockLocationKey));
                    // ->
                }

            }
        }
    }

    @EventHandler
    public void onCustomBlockPlace(CustomBlockPlaceEvent event) throws IOException {
        if (doUseCustomBlock()) {
            String customBlockNameSpace = HomewardCooking.configurationLoader.getGeneralSettings().getString("custom-blocks-namespace");
            String namespacedID = event.getNamespacedID();
            if (Objects.equals(namespacedID, customBlockNameSpace)) {
                Location location = event.getBlock().getLocation();
                NBTFile file = new NBTFile(new File(event.getPlayer().getWorld().getWorldFolder().getName(), "cooking-data.nbt"));
                int blockX = location.getBlockX();
                int blockY = location.getBlockY();
                int blockZ = location.getBlockZ();

                String locationKey = location.getWorld() + " " + blockX + " " + blockY + " " + blockZ;

                if (!file.hasKey(locationKey)) {
                    file.setObject(locationKey, StreamItemsUtils.serializeAsBytes(new CookingData()));
                    Bukkit.getScheduler().runTaskAsynchronously(HomewardCooking.getInstance(), () -> {
                        try {
                            file.save();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    event.getPlayer().sendMessage("设置物品信息成功");
                }
            }
        }
    }
}
