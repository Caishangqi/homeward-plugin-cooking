package homeward.plugin.homewardcooking.utils.loaders;

import homeward.plugin.homewardcooking.Homewardcooking;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.DictionaryLabel;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.RecipeContent;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import homeward.plugin.homewardcooking.utils.Type;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

public class DictionaryLoader {

    private HashMap<String, FileConfiguration> loadedDictionaryFiles = new HashMap<>();

    private File dictionaryFolder = new File(Homewardcooking.getInstance().getDataFolder(), "dictionary");

    private HashMap<String, DictionaryLabel> loadedDictionary = new HashMap<>();

    private File accessRawYMLFile[];

    public void importDictionary() {
        accessRawYMLFile = dictionaryFolder.listFiles();
        if (accessRawYMLFile.length >= 1) {
            for (File file : accessRawYMLFile) {
                if (file.isFile()) {
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                    if (!configuration.getKeys(false).isEmpty()) {
                        loadedDictionaryFiles.put(file.getName(), configuration);
                    }
                }
            }

        }

        turnConfigFileToDictionary();

    }

    private void turnConfigFileToDictionary() {

        for (FileConfiguration configuration : loadedDictionaryFiles.values()) {
            Set<String> keys = configuration.getKeys(false);

            Iterator<String> it = keys.iterator();
            while (it.hasNext() && !keys.isEmpty()) {
                String key = it.next();
                try {
                    loadSingleDictionary(key, configuration);
                } catch (Exception exception) {
                    CommonUtils.getInstance().log(Level.WARNING, Type.FATAL, "字典 " + key + " 加载失败");
                    exception.printStackTrace();
                }

            }
        }

    }

    private void loadSingleDictionary(String key, FileConfiguration configuration) {
        DictionaryLabel dictionaryLabel = new DictionaryLabel();
        Set<String> containedItemsInDictionary = configuration.getKeys(false);
        RecipeContent recipeContent = new RecipeContent();
        for (String itemNumber : containedItemsInDictionary) {
            ConfigurationSection configurationSectionInNumber = configuration.getConfigurationSection(itemNumber);
            for (String keys : configurationSectionInNumber.getKeys(false)) {

                switch (keys) {
                    case "type":
                        String type = configurationSectionInNumber.getString("type");
                        recipeContent.setType(type);
                        break;
                    case "material":
                        String material = configurationSectionInNumber.getString("material");
                        recipeContent.setMaterial(material);
                }


            }
            dictionaryLabel.getDictionaryIncluded().add(recipeContent);
            dictionaryLabel.setDictionaryName(key);
        }


        if (loadedDictionary.containsKey(key)) {
            CommonUtils.getInstance().log(Level.INFO, Type.FATAL, "你的词典 " + " " + key + " 是重复的");

        } else {
            loadedDictionary.put(key, dictionaryLabel);
            CommonUtils.getInstance().log(Level.INFO, Type.LOADED, "词典 " + key + " 加载成功");
        }

    }

    public HashMap<String, DictionaryLabel> getLoadedDictionary() {
        return loadedDictionary;
    }

    public void setLoadedDictionary(HashMap<String, DictionaryLabel> loadedDictionary) {
        this.loadedDictionary = loadedDictionary;
    }
}
