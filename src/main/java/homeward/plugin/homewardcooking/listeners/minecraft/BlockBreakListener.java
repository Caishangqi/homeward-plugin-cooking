package homeward.plugin.homewardcooking.listeners.minecraft;

import de.tr7zw.changeme.nbtapi.NBTFile;
import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.pojo.CookingData;
import homeward.plugin.homewardcooking.pojo.CookingPotThing;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.bukkit.Material.CAULDRON;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) throws IOException {

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
                CookingData cookingData = file.getObject(locationKey, CookingData.class);
                List<ItemStack> containedItemsInData = CommonUtils.getContainedItemsInData(cookingData);

                event.setDropItems(false);
                file.removeKey(locationKey);
                file.save();
                player.sendMessage("移除物品数据成功");
                //聪明代码
                Bukkit.getServer().getWorld(event.getPlayer().getWorld().getName()).dropItem(breakBlock.getLocation(), CookingPotThing.getVanillaItemStack());
                containedItemsInData.forEach(K -> {
                    if (!K.getType().isAir())
                        Bukkit.getServer().getWorld(event.getPlayer().getWorld().getName()).dropItem(breakBlock.getLocation(), K);
                });

            }

        }

    }
}
