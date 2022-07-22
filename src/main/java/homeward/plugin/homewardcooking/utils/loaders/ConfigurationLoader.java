package homeward.plugin.homewardcooking.utils.loaders;

import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.pojo.Button;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;

public class ConfigurationLoader {

    private FileConfiguration mainConfiguration;
    private FileConfiguration databaseConfiguration;
    private FileConfiguration messageConfiguration;

    public ConfigurationLoader () {
        loadConfiguration();
        setEnumInformation();
    }

    public void loadConfiguration() {

        mainConfiguration = HomewardCooking.getInstance().getConfig();
        databaseConfiguration = YamlConfiguration.loadConfiguration(new File(HomewardCooking.getInstance().getDataFolder(), "database"));
        messageConfiguration = YamlConfiguration.loadConfiguration(new File(HomewardCooking.getInstance().getDataFolder(), "message"));

        setEnumInformation();
    }

    private void setEnumInformation() {
        Button.RECIPE_BUTTON.setSlot(mainConfiguration.getInt("gui-settings.recipes-button"));
        Button.START_BUTTON.setSlot(mainConfiguration.getInt("gui-settings.start-button"));
        Button.PROCESS_BUTTON.setSlot(mainConfiguration.getInt("gui-settings.process-slot"));
        Button.READY_BUTTON.setSlot(mainConfiguration.getInt("gui-settings.process-slot"));
        String filledItems = mainConfiguration.getString("gui-settings.filled-item", "GRAY_STAINED_GLASS_PANE");
        Button.FILLED_BUTTON.setMaterial(Material.valueOf(filledItems));
    }

    public Integer getGUIInputSlot(Integer slot) {
        switch (slot) {
            case 1:
                return mainConfiguration.getInt("gui-settings.first-input-slot", 10);
            case 2:
                return mainConfiguration.getInt("gui-settings.second-input-slot", 19);
            case 3:
                return mainConfiguration.getInt("gui-settings.third-input-slot", 28);
            case 4:
                return mainConfiguration.getInt("gui-settings.fourth-input-slot", 37);
        }
        return null;
    }

    public int[] getGUIInputSlot() {
        return new int[]{getGUIInputSlot(1), getGUIInputSlot(2), getGUIInputSlot(3), getGUIInputSlot(4)};
    }

    public Integer getGUIOutputSlot() {
        return mainConfiguration.getInt("gui-settings.output-slot", 22);
    }


}
