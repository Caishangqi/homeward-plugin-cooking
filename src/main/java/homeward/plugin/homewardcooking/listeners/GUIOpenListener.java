package homeward.plugin.homewardcooking.listeners;

import de.tr7zw.changeme.nbtapi.NBTFile;
import homeward.plugin.homewardcooking.Homewardcooking;
import homeward.plugin.homewardcooking.events.GUIOpenEvent;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.CookingData;
import homeward.plugin.homewardcooking.utils.GUIManipulation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;

public class GUIOpenListener implements Listener {

    @EventHandler
    public void onGUIOpen(GUIOpenEvent event) throws IOException, ClassNotFoundException {
        String locationKey = event.getLocationKey();
        Player player = event.getPlayer();
        NBTFile file = new NBTFile(new File(event.getPlayer().getWorld().getWorldFolder().getName(), "cooking-data.nbt"));

        if (Homewardcooking.GUIPools.containsKey(locationKey)) {
            CookingGUI currentPotGUI = Homewardcooking.GUIPools.get(locationKey);
            currentPotGUI.addPlayerToOpenPlayers(player);
            CookingData cookingData = file.getObject(locationKey, CookingData.class);
            //数据载入
            GUIManipulation.dataInjectionToGUI(cookingData, currentPotGUI);
            //数据载入结束
            currentPotGUI.open(player);
            player.swingMainHand();
        } else {
            CookingGUI cookingGUI = new CookingGUI();
            cookingGUI.setGuiName("厨艺锅");
            //数据载入

            //数据载入结束
            cookingGUI.open(player);
            cookingGUI.addPlayerToOpenPlayers(player);
            cookingGUI.setLocationKey(locationKey);
            player.swingMainHand();
            Homewardcooking.GUIPools.put(locationKey, cookingGUI);
        }


    }
}
