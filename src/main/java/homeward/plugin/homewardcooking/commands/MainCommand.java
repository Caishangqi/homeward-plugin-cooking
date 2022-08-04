package homeward.plugin.homewardcooking.commands;

import de.tr7zw.changeme.nbtapi.NBTCompound;
import de.tr7zw.changeme.nbtapi.NBTItem;
import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.pojo.CookingPotThing;
import homeward.plugin.homewardcooking.pojo.CookingProcessObject;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.DictionaryLabel;
import homeward.plugin.homewardcooking.utils.CommonUtils;
import homeward.plugin.homewardcooking.utils.StreamItemsUtils;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

@Command("hwc")
public class MainCommand extends CommandBase {

//    @SubCommand("nms")
//    public void testNMS(CommandSender commandSender) {
//        CraftPlayer craftPlayer = (CraftPlayer) commandSender; //CraftBukkit
//        EntityPlayer entityPlayer = craftPlayer.getHandle(); //NMS
//
//        PlayerConnection playerConnection = entityPlayer.b;
//        PacketPlayOutGameStateChange packet = new PacketPlayOutGameStateChange(PacketPlayOutGameStateChange.a, 0.0f);
//        playerConnection.a(packet);
//
//        IChatBaseComponent[] test = {IChatBaseComponent.a("test")};
//        entityPlayer.sendMessage(entityPlayer.getBukkitEntity().getUniqueId(), test);
//
//        //Use NMS
//
//    }

    @SubCommand("mojangnms")
    public void testMojangMappingNMS(CommandSender commandSender) {

        CraftPlayer craftPlayer = (CraftPlayer) commandSender; //CraftBukkit
        ServerPlayer serverPlayer = craftPlayer.getHandle(); //NMS

        ServerGamePacketListenerImpl listener = serverPlayer.connection;
        ClientboundGameEventPacket packet = new ClientboundGameEventPacket(ClientboundGameEventPacket.STOP_RAINING, 0.0f);
        listener.send(packet);

        serverPlayer.sendMessage(Component.nullToEmpty("Sending weather change"), serverPlayer.getUUID());

    }

    @SubCommand("checkPool")
    public void checkPool(CommandSender commandSender) {
        Player player = (Player) commandSender;
        player.sendMessage("当前GUI池中缓存的GUI数量为 " + HomewardCooking.GUIPools.size() + " 个");

    }

    @SubCommand("removePool")
    public void removePool(CommandSender commandSender) {
        Player player = (Player) commandSender;
        HomewardCooking.GUIPools.clear();
        player.sendMessage("GUI池清除成功！");

    }

    @SubCommand("give")
    @Completion({"#players", "#range:1-64"})
    public void giveCookingPot(CommandSender commandSender, final Player player, final Integer number) {


        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6给予 " + player.getDisplayName() + " &6厨艺锅"));
        ItemStack cauldron = CookingPotThing.getVanillaItemStack();
        cauldron.setAmount(number);
        player.getInventory().addItem(cauldron);


    }

    @SubCommand("recipes")
    public void showRecipes(CommandSender commandSender) {
        Player player = (Player) commandSender;

        HashMap<String, CookingRecipe> loadRecipes = HomewardCooking.recipesLoader.getLoadRecipes();
        Set<String> strings = loadRecipes.keySet();
        CommonUtils.sendPluginMessageInServer(player, "&r&l当前加载的配方: &7" + strings);
    }

    @SubCommand("reload")
    public void reloadPlugin(CommandSender commandSender, @Completion("#reloadType") @Optional final String type) {
        if (type != null) {
            switch (type) {
                case "recipe":
                    CommonUtils.reloadRecipe();
                    CommonUtils.sendPluginMessageInServer((Player) commandSender, "配方重载成功");
                    break;
                case "dictionary":
                    CommonUtils.reloadDictionary();
                    CommonUtils.sendPluginMessageInServer((Player) commandSender, "词典重载成功");

            }
        } else {
            CommonUtils.reloadPlugin();
            CommonUtils.sendPluginMessageInServer((Player) commandSender, "插件重载成功");

        }
    }

    @SubCommand("dictionary")
    public void showDictionary(CommandSender commandSender) {
        Player player = (Player) commandSender;

        HashMap<String, DictionaryLabel> loadedDictionary = HomewardCooking.dictionaryLoader.getLoadedDictionary();
        Set<String> strings = loadedDictionary.keySet();
        CommonUtils.sendPluginMessageInServer(player, "&r&l当前加载的词典: &7" + strings);
    }

    @SubCommand("onprocess")
    public void showOnProcess(CommandSender commandSender) {
        Player player = (Player) commandSender;

        HashMap<Location, CookingProcessObject> processPool = HomewardCooking.processPool;
        try {
            Set<Location> strings = processPool.keySet();
            CommonUtils.sendPluginMessageInServer(player, "&r&l当前正在进行的配方: &7");
            for (Location l : strings) {
                CommonUtils.sendPluginMessageInServer(player, "&r&l: &7 世界 - " + l.getWorld().getName() + " &c输出 &7- " + processPool.get(l).getCookingRecipe().getMainOutPut().getMaterial());
            }
        } catch (Exception e) {
            CommonUtils.sendPluginMessageInServer(player, "&6&l当前没有正在进行的配方");
        }


    }


    @Default
    public void defaultCommand(final CommandSender commandSender) {


    }
}
