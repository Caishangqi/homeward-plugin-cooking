package homeward.plugin.homewardcooking.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CookingInitialEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final boolean async = true;
    private Player player;
    private List<ItemStack> containedMaterial;
    private String locationKey;
    private ItemStack remainedOutPutStack;
    private boolean cancelledFlag = false;

    public CookingInitialEvent(Player player, List<ItemStack> containedMaterial, String locationKey) {
        this.player = player;
        this.containedMaterial = containedMaterial;
        this.locationKey = locationKey;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public ItemStack getRemainedOutPutStack() {

        return remainedOutPutStack;

    }

    public void setRemainedOutPutStack(ItemStack remainedOutPutStack) {
        this.remainedOutPutStack = remainedOutPutStack;
    }

    @Override
    public boolean isCancelled() {
        return cancelledFlag;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelledFlag = cancel;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<ItemStack> getContainedMaterial() {
        return containedMaterial;
    }

    public void setContainedMaterial(List<ItemStack> containedMaterial) {
        this.containedMaterial = containedMaterial;
    }

    public void setCancelledFlag(boolean cancelledFlag) {
        this.cancelledFlag = cancelledFlag;
    }

    public String getLocationKey() {
        return locationKey;
    }

    public void setLocationKey(String locationKey) {
        this.locationKey = locationKey;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
