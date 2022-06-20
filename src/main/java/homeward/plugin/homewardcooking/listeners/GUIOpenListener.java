package homeward.plugin.homewardcooking.listeners;

import homeward.plugin.homewardcooking.events.GUIOpenEvent;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class GUIOpenListener implements Listener {

    @EventHandler
    public void onGUIOpen(GUIOpenEvent event) {
        Player player = event.getPlayer();
        CookingGUI cookingGUI = new CookingGUI();
        cookingGUI.setGuiName("厨艺锅");
        cookingGUI.open(player);
    }
}
