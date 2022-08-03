package homeward.plugin.homewardcooking.events;

import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class CookingProcessEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private Player whoCalled;
    private CookingRecipe targetRecipe;
    private String locationKey;
    private boolean cancelledFlag = false;

    public CookingProcessEvent(Player player, CookingRecipe cookingRecipe, String locationKey) {
        this.whoCalled = player;
        this.targetRecipe = cookingRecipe;
        this.locationKey = locationKey;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getWhoCalled() {
        return whoCalled;
    }

    public void setWhoCalled(Player whoCalled) {
        this.whoCalled = whoCalled;
    }

    public CookingRecipe getTargetRecipe() {
        return targetRecipe;
    }

    public void setTargetRecipe(CookingRecipe targetRecipe) {
        this.targetRecipe = targetRecipe;
    }

    public String getLocationKey() {
        return locationKey;
    }

    public void setLocationKey(String locationKey) {
        this.locationKey = locationKey;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelledFlag = cancel;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
