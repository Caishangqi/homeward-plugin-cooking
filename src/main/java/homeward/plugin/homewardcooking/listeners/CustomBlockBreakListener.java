package homeward.plugin.homewardcooking.listeners;

import dev.lone.itemsadder.api.Events.CustomBlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

//Just a Test Listener
public class CustomBlockBreakListener implements Listener {

    @EventHandler
    private void onCustomBlockBreakEvent(CustomBlockBreakEvent e) {
        System.out.println(e.getClass().getName() + " " + e.getPlayer().getName() + " " + e.getNamespacedID());
    }

}
