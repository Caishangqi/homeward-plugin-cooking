package homeward.plugin.homewardcooking.compatibilities.provided.minecraft;

import de.tr7zw.changeme.nbtapi.NBTFile;
import de.tr7zw.changeme.nbtapi.NBTItem;
import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.compatibilities.CompatibilityPlugin;
import homeward.plugin.homewardcooking.events.GUIOpenEvent;
import homeward.plugin.homewardcooking.pojo.CookingData;
import homeward.plugin.homewardcooking.pojo.CookingPotThing;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import homeward.plugin.homewardcooking.utils.StreamItemsUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.bukkit.Material.CAULDRON;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;
import static org.bukkit.inventory.EquipmentSlot.HAND;

public class MinecraftCompatibility extends CompatibilityPlugin {

    public static boolean isSimilar(ItemStack firstItems, ItemStack secondItems) {
        //Vanilla method
        return firstItems.isSimilar(secondItems);
    }

    private boolean doUseVanillaBlock() {
        //这个默认是False
        return HomewardCooking.configurationLoader.getGeneralSettings().getBoolean("use-vanilla-blocks", true);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) throws IOException {

        if (doUseVanillaBlock()) {
            Player player = event.getPlayer();

            if (event.getBlock().getBlockData().getMaterial() == CAULDRON) {
                NBTFile file = new NBTFile(new File(event.getPlayer().getWorld().getWorldFolder().getName(), "cooking-data.nbt"));

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

                    event.setDropItems(false);
                    file.removeKey(locationKey);
                    file.save();
                    player.sendMessage("移除物品数据成功");
                    //聪明代码
                    Bukkit.getServer().getWorld(event.getPlayer().getWorld().getName()).dropItem(breakBlock.getLocation(), CookingPotThing.getVanillaItemStack());
                    containedItemsInData.forEach(K -> {
                        if (K != null)
                            Bukkit.getServer().getWorld(event.getPlayer().getWorld().getName()).dropItem(breakBlock.getLocation(), K);
                    });

                }

            }

        }

    }

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) throws IOException {

        if (doUseVanillaBlock()) {
            Player player = event.getPlayer();
            Action action = event.getAction();
            if (event.getClickedBlock() != null && action == RIGHT_CLICK_BLOCK) {
                Block clickedBlock = event.getClickedBlock();

                int blockX = clickedBlock.getLocation().getBlockX();
                int blockY = clickedBlock.getLocation().getBlockY();
                int blockZ = clickedBlock.getLocation().getBlockZ();

                NBTFile file = new NBTFile(new File(event.getPlayer().getWorld().getWorldFolder().getName(), "cooking-data.nbt"));

                String locationKey = clickedBlock.getWorld() + " " + blockX + " " + blockY + " " + blockZ;

                //PlayerInteractEvent默认主手副手都触发 除非判断主手
                if (file.hasKey(locationKey) && event.getHand() == HAND) {
                    Bukkit.getServer().getPluginManager().callEvent(new GUIOpenEvent(player, locationKey));
                    // ->
                }

            }

        }

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) throws IOException {

        if (doUseVanillaBlock()) {
            Player player = event.getPlayer();

            ItemStack itemInHand = event.getItemInHand();
            NBTItem nbti = new NBTItem(itemInHand);
            Block blockPlaced = event.getBlockPlaced();
            if (blockPlaced.getBlockData().getMaterial() == CAULDRON && nbti.hasKey("CookingPot")) {
                System.out.println(itemInHand.getType());
                NBTFile file = new NBTFile(new File(event.getPlayer().getWorld().getWorldFolder().getName(), "cooking-data.nbt"));
                int blockX = blockPlaced.getLocation().getBlockX();
                int blockY = blockPlaced.getLocation().getBlockY();
                int blockZ = blockPlaced.getLocation().getBlockZ();

                String locationKey = blockPlaced.getWorld() + " " + blockX + " " + blockY + " " + blockZ;

                if (!file.hasKey(locationKey)) {
                    file.setObject(locationKey, StreamItemsUtils.serializeAsBytes(new CookingData()));
                    Bukkit.getScheduler().runTaskAsynchronously(HomewardCooking.getInstance(), () -> {
                        try {
                            file.save();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

                    player.sendMessage("设置物品信息成功");
                }


            }


        }
    }
}



