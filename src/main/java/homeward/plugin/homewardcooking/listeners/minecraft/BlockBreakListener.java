package homeward.plugin.homewardcooking.listeners.minecraft;

import de.tr7zw.changeme.nbtapi.NBTFile;
import homeward.plugin.homewardcooking.pojo.CookingPotThing;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.io.File;
import java.io.IOException;

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

            String locationKey = "" + blockX + "" + blockY + "" + blockZ;

            if (file.hasKey(locationKey)) {

                event.setDropItems(false);
                file.removeKey(locationKey);
                file.save();
                player.sendMessage("移除物品数据成功");
                //聪明代码
                Bukkit.getServer().getWorld(event.getPlayer().getWorld().getName()).dropItem(breakBlock.getLocation(), CookingPotThing.getVanillaItemStack());

            }

        }

    }
}
