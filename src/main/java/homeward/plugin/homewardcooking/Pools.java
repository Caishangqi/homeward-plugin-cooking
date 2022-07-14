package homeward.plugin.homewardcooking;

import homeward.plugin.homewardcooking.pojo.CookingData;
import homeward.plugin.homewardcooking.pojo.CookingProcessObject;
import org.bukkit.Location;

import java.util.HashMap;

public class Pools {

    private HashMap <Location, CookingProcessObject> processPool;

    public HashMap<Location, CookingProcessObject> getProcessPool() {
        return processPool;
    }

    public void setProcessPool(HashMap<Location, CookingProcessObject> processPool) {
        this.processPool = processPool;
    }
}
