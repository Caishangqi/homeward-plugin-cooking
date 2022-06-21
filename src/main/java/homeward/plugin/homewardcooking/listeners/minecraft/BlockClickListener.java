package homeward.plugin.homewardcooking.listeners.minecraft;

import de.tr7zw.changeme.nbtapi.NBTFile;
import homeward.plugin.homewardcooking.events.GUIOpenEvent;
import homeward.plugin.homewardcooking.pojo.CookingData;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.io.File;
import java.io.IOException;

import static org.bukkit.event.block.Action.LEFT_CLICK_BLOCK;
import static org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK;

public class BlockClickListener implements Listener {

    @EventHandler
    public void onBlockClick(PlayerInteractEvent event) throws IOException {
        Player player = event.getPlayer();
        Action action = event.getAction();
        if (event.getClickedBlock() != null && action == RIGHT_CLICK_BLOCK) {
            Block clickedBlock = event.getClickedBlock();

            int blockX = clickedBlock.getLocation().getBlockX();
            int blockY = clickedBlock.getLocation().getBlockY();
            int blockZ = clickedBlock.getLocation().getBlockZ();

            NBTFile file = new NBTFile(new File(event.getPlayer().getWorld().getWorldFolder().getName(), "cooking-data.nbt"));

            String locationKey = "" + blockX + "" + blockY + "" + blockZ;

            if (file.hasKey(locationKey)) {

                Bukkit.getServer().getPluginManager().callEvent(new GUIOpenEvent(player,locationKey));

                // ->
            }

        }

    }
}
