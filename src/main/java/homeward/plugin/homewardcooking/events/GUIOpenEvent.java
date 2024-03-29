package homeward.plugin.homewardcooking.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GUIOpenEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final boolean async = true;
    private Player player;
    private String locationKey;
    private boolean cancelledFlag = false;

    public GUIOpenEvent(Player player, String locationKey) {
        this.player = player;
        this.locationKey = locationKey;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getLocationKey() {
        return locationKey;
    }

    public void setLocationKey(String locationKey) {
        this.locationKey = locationKey;
    }

    @Override
    public boolean isCancelled() {
        return cancelledFlag;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelledFlag = cancel;
    }

    @NotNull
    public HandlerList getHandlers() {
        return handlers;
    }


}
