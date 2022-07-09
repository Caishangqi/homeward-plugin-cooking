package homeward.plugin.homewardcooking.utils;

import homeward.plugin.homewardcooking.Homewardcooking;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.RecipeContent;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;

public class RecipesLoader {

    //储存读取到了yml配方文件并以文件名储存
    private HashMap<String, FileConfiguration> loadedRecipesFiles = new HashMap<>();
    //储存转换FileConfiguration的配方到CookingRecipes里
    private HashMap<String, CookingRecipe> loadRecipes = new HashMap<>();
    private File accessRawYMLFile[];
    //
    private File pluginFolder = new File(Homewardcooking.getInstance().getDataFolder(), "recipes");

    public void importRecipes() {
        //开始从文件形式导入到YamlConfiguration， 3个文件就有3个YamlConfiguration
        accessRawYMLFile = pluginFolder.listFiles();
        if (accessRawYMLFile.length >= 1) {
            for (File file : accessRawYMLFile) {
                if (file.isFile()) {
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(file);
                    if (!configuration.getKeys(false).isEmpty()) {
                        loadedRecipesFiles.put(file.getName(), configuration);
                    }
                }
            }

        }
        //导入完毕YamlConfiguration后把每一个YamlConfiguration里面的所有key转换成配方

        turnConfigFileToRecipes();

    }

    private void turnConfigFileToRecipes() {

        for (FileConfiguration configuration : loadedRecipesFiles.values()) {
            Set<String> keys = configuration.getKeys(false);

            Iterator<String> it = keys.iterator();
            while (it.hasNext()) {
                String key = it.next();
                loadSingleRecipe(key, configuration);
            }
        }
    }

    //将一个FileConfiguration文件里的所有配方加载
    private void loadSingleRecipe(String key, FileConfiguration configuration) {
        CookingRecipe cookingRecipe = new CookingRecipe(); //先创建一个配方对象
        //从recipe-input截取getConfigurationSection获取他的部分
        ConfigurationSection recipeInputs = configuration.getConfigurationSection(key + ".recipe-inputs");
        //获取所有recipe-inputs下的keys 1,2,3,4
        Set<String> keys = recipeInputs.getKeys(false);

        //遍历这些keys
        Iterator<String> it = keys.iterator(); //1,2,3,4
        //如果有这个key
        while (it.hasNext()) {
            //新建一个配方材料pojo
            RecipeContent recipeContent = new RecipeContent();
            String input = it.next(); //当前是哪个key 1,2,3,4
            //截取1,下面的keys
            ConfigurationSection inputsConfigurationSection = recipeInputs.getConfigurationSection(input); //1
            Set<String> inputsConfigurationSectionKeys = inputsConfigurationSection.getKeys(false); //time-required etc.

            for (String detailedKeys : inputsConfigurationSectionKeys) {
                switch (detailedKeys) {
                    case "time-required":
                        String timeRequired = inputsConfigurationSection.getString("time-required");
                        recipeContent.setTimeRequired(Integer.valueOf(timeRequired));
                        break;
                    case "type":
                        String type = inputsConfigurationSection.getString("type");
                        recipeContent.setType(type);
                        break;
                    case "material":
                        String material = inputsConfigurationSection.getString("material");
                        recipeContent.setMaterial(material);
                }
            }

            cookingRecipe.getContents().add(recipeContent);

        }
        if (loadRecipes.containsKey(key)) {
            CommonUtils.getInstance().log(Level.WARNING, Type.FATAL, "你的配方" + " " + key + " 是重复的");

        } else {
            loadRecipes.put(key, cookingRecipe);
            CommonUtils.getInstance().log(Level.INFO, Type.LOADED, "配方" + key + "加载成功");
        }


    }

    public HashMap<String, CookingRecipe> getLoadRecipes() {
        return loadRecipes;
    }

    public void setLoadRecipes(HashMap<String, CookingRecipe> loadRecipes) {
        this.loadRecipes = loadRecipes;
    }
}
