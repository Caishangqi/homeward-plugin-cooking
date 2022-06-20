package homeward.plugin.homewardcooking.listeners;

import homeward.plugin.homewardcooking.Homewardcooking;
import homeward.plugin.homewardcooking.events.GUIOpenEvent;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GUIOpenListener implements Listener {

    @EventHandler
    public void onGUIOpen(GUIOpenEvent event) {
        String locationKey = event.getLocationKey();
        Player player = event.getPlayer();

        if (Homewardcooking.GUIPools.containsKey(locationKey)) {
            CookingGUI currentPotGUI = Homewardcooking.GUIPools.get(locationKey);
            currentPotGUI.addPlayerToOpenPlayers(player);
            currentPotGUI.open(player);
            player.swingMainHand();
        } else {
            CookingGUI cookingGUI = new CookingGUI();
            cookingGUI.setGuiName("厨艺锅");
            cookingGUI.open(player);
            cookingGUI.addPlayerToOpenPlayers(player);
            player.swingMainHand();
            Homewardcooking.GUIPools.put(locationKey, cookingGUI);
        }


    }
}
