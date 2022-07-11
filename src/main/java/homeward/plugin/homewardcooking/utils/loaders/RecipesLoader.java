package homeward.plugin.homewardcooking.utils.loaders;

import homeward.plugin.homewardcooking.Homewardcooking;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
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
            while (it.hasNext() && !keys.isEmpty()) {
                String key = it.next();
                try {
                    loadSingleRecipe(key, configuration);
                } catch (Exception exception) {
                    CommonUtils.getInstance().log(Level.WARNING, Type.FATAL, "配方 " + key + " 加载失败");
                    exception.printStackTrace();
                }

            }
        }
    }

    //将一个FileConfiguration文件里的所有配方加载
    private void loadSingleRecipe(String key, FileConfiguration configuration) {
        CookingRecipe cookingRecipe = new CookingRecipe(); //先创建一个配方对象
        Boolean validRecipe = true; //检查是否符合配方标准

        //从recipe-input截取getConfigurationSection获取他的部分
        ConfigurationSection recipeInputs = configuration.getConfigurationSection(key + ".recipe-inputs");
        //从recipe-outputs截取main-output从getConfigurationSection获取他的部分
        ConfigurationSection recipeMainOutput = configuration.getConfigurationSection(key + ".recipe-outputs.main-output");
        ConfigurationSection additionalOutPut = configuration.getConfigurationSection(key + ".recipe-outputs.additional-output");
        //获取所有recipe-inputs下的keys 1,2,3,4
        Set<String> keys = recipeInputs.getKeys(false);
        Set<String> recipeMainOutputKeys = recipeMainOutput.getKeys(false);
        Set<String> additionalOutPutKeys = additionalOutPut.getKeys(false);


        //遍历这些keys
        Iterator<String> it = keys.iterator(); //1,2,3,4
        Iterator<String> itOfRecipeMainOutputKeys = recipeMainOutputKeys.iterator();
        Iterator<String> itOfAdditionalOutPutKeys = additionalOutPutKeys.iterator();

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
                        if (!recipeContent.setMaterial(material, key)) {
                            validRecipe = false;
                        }


                }
            }

            cookingRecipe.getContents().add(recipeContent);

        }

        while (itOfRecipeMainOutputKeys.hasNext()) {
            RecipeContent recipeOutputContent = new RecipeContent();
            String input = itOfRecipeMainOutputKeys.next();
            switch (input) {
                case "type":
                    String type = recipeMainOutput.getString("type");
                    recipeOutputContent.setType(type);
                    break;
                case "material":
                    String material = recipeMainOutput.getString("material");
                    recipeOutputContent.setType(material);
            }
            cookingRecipe.setMainOutPut(recipeOutputContent);
        }

        while (itOfAdditionalOutPutKeys.hasNext()) {
            String next = itOfAdditionalOutPutKeys.next();
            RecipeContent recipeAdditionalOutPutContent = new RecipeContent();
            ConfigurationSection additionalOutPutConfigurationSection = additionalOutPut.getConfigurationSection(next);
            Set<String> sectionKeys = additionalOutPutConfigurationSection.getKeys(false);
            for (String numberKeys : sectionKeys) {
                switch (numberKeys) {
                    case "type":
                        String type = additionalOutPutConfigurationSection.getString("type");
                        recipeAdditionalOutPutContent.setType(type);
                        break;
                    case "material":
                        String material = additionalOutPutConfigurationSection.getString("material");
                        if (!recipeAdditionalOutPutContent.setMaterial(material, key)) {
                            validRecipe = false;
                        }
                        break;
                    case "command":
                        String command = additionalOutPutConfigurationSection.getString("command");
                        recipeAdditionalOutPutContent.setCommand(command);
                }
            }
            cookingRecipe.getAdditionalOutPut().add(recipeAdditionalOutPutContent);
        }

        if (loadRecipes.containsKey(key)) {
            CommonUtils.getInstance().log(Level.INFO, Type.FATAL, "你的配方" + " " + key + " 是重复的");

        } else if (!validRecipe) {
            CommonUtils.getInstance().log(Level.INFO, Type.FATAL, "你的配方" + " " + key + " 加载失败");
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
