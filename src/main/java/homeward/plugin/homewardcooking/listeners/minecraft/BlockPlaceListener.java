package homeward.plugin.homewardcooking.listeners.minecraft;

import de.tr7zw.changeme.nbtapi.NBTFile;
import de.tr7zw.changeme.nbtapi.NBTItem;
import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.pojo.CookingData;
import homeward.plugin.homewardcooking.utils.StreamItemsUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;

import static org.bukkit.Material.CAULDRON;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) throws IOException {
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
