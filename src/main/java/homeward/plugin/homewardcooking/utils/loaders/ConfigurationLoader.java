package homeward.plugin.homewardcooking.utils.loaders;

import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.pojo.Button;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigurationLoader {

    private FileConfiguration mainConfiguration;
    private FileConfiguration databaseConfiguration;
    private FileConfiguration messageConfiguration;

    public ConfigurationLoader() {
        loadConfiguration();
    }

    public void loadConfiguration() {


        mainConfiguration = YamlConfiguration.loadConfiguration(new File(HomewardCooking.getInstance().getDataFolder(), "config.yml"));
        databaseConfiguration = YamlConfiguration.loadConfiguration(new File(HomewardCooking.getInstance().getDataFolder(), "database.yml"));
        messageConfiguration = YamlConfiguration.loadConfiguration(new File(HomewardCooking.getInstance().getDataFolder(), "message.yml"));

        setEnumInformation();
    }

    private void setEnumInformation() {

        Button.RECIPE_BUTTON.setSlot(mainConfiguration.getInt("gui-settings.recipes-button.slot"));
        Button.RECIPE_BUTTON.setMaterial(Material.valueOf(mainConfiguration.getString("gui-settings.recipes-button.Material", "KNOWLEDGE_BOOK")));
        Button.RECIPE_BUTTON.setCustomModelData(mainConfiguration.getInt("gui-settings.recipes-button.custom-model-data"));

        Button.START_BUTTON.setSlot(mainConfiguration.getInt("gui-settings.start-button.slot"));
        Button.START_BUTTON.setMaterial(Material.valueOf(mainConfiguration.getString("gui-settings.start-button.Material", "OAK_BUTTON")));
        Button.START_BUTTON.setCustomModelData(mainConfiguration.getInt("gui-settings.start-button.custom-model-data"));

        Button.PROCESS_BUTTON.setSlot(mainConfiguration.getInt("gui-settings.process-slot"));
        Button.PROCESS_BUTTON.setCustomModelData(mainConfiguration.getInt("gui-state-process-settings.discontinuous-state-settings.processing.custom-model-data"));
        Button.PROCESS_BUTTON.setMaterial(Material.valueOf(mainConfiguration.getString("gui-state-process-settings.discontinuous-state-settings.processing.Material", "ORANGE_STAINED_GLASS_PANE")));

        Button.READY_BUTTON.setSlot(mainConfiguration.getInt("gui-settings.process-slot"));
        Button.READY_BUTTON.setCustomModelData(mainConfiguration.getInt("gui-state-process-settings.discontinuous-state-settings.ready.custom-model-data"));
        Button.READY_BUTTON.setMaterial(Material.valueOf(mainConfiguration.getString("gui-state-process-settings.discontinuous-state-settings.ready.Material", "GREEN_STAINED_GLASS_PANE")));

        Button.FILLED_BUTTON.setMaterial(Material.valueOf(mainConfiguration.getString("gui-settings.filled-item", "GRAY_STAINED_GLASS_PANE")));

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

    public String getGUITitle() {
        return mainConfiguration.getString("gui-settings.gui-name", "Cooking Pot");
    }

}
