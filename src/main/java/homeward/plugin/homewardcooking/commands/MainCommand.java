
package homeward.plugin.homewardcooking.commands;

import homeward.plugin.homewardcooking.HomewardCooking;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.CookingPotThing;
import homeward.plugin.homewardcooking.pojo.CookingProcessObject;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.CookingRecipe;
import homeward.plugin.homewardcooking.pojo.cookingrecipe.DictionaryLabel;
import homeward.plugin.homewardcooking.utils.CommonUtils;
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
        player.sendMessage(String.valueOf("??????GUI???????????????GUI????????? " + HomewardCooking.GUIPools.size() + " ???"));

    }

    @SubCommand("removePool")
    public void removePool(CommandSender commandSender) {
        Player player = (Player) commandSender;
        HomewardCooking.GUIPools.clear();
        player.sendMessage("GUI??????????????????");

    }

    @SubCommand("give")
    @Completion({"#players", "#range:1-64"})
    public void giveCookingPot(CommandSender commandSender, final Player player, final Integer number) {


        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6?????? " + player.getDisplayName() + " &6?????????"));
        ItemStack cauldron = CookingPotThing.getVanillaItemStack();
        cauldron.setAmount(number);
        player.getInventory().addItem(cauldron);


    }

    @SubCommand("recipes")
    public void showRecipes(CommandSender commandSender) {
        Player player = (Player) commandSender;

        HashMap<String, CookingRecipe> loadRecipes = HomewardCooking.recipesLoader.getLoadRecipes();
        Set<String> strings = loadRecipes.keySet();
        CommonUtils.getInstance().sendPluginMessageInServer(player, "&r&l?????????????????????: &7" + String.valueOf(strings));
    }

    @SubCommand("reload")
    public void reloadPlugin(CommandSender commandSender, @Completion("#reloadType") @Optional final String type) {
        if (type != null) {
            switch (type) {
                case "recipe":
                    CommonUtils.getInstance().reloadRecipe();
                    CommonUtils.getInstance().sendPluginMessageInServer((Player) commandSender, "??????????????????");
                    break;
                case "dictionary":
                    CommonUtils.getInstance().reloadDictionary();
                    CommonUtils.getInstance().sendPluginMessageInServer((Player) commandSender, "??????????????????");

            }
        } else {
            CommonUtils.getInstance().reloadPlugin();
            CommonUtils.getInstance().sendPluginMessageInServer((Player) commandSender, "??????????????????");

        }
    }

    @SubCommand("dictionary")
    public void showDictionary(CommandSender commandSender) {
        Player player = (Player) commandSender;

        HashMap<String, DictionaryLabel> loadedDictionary = HomewardCooking.dictionaryLoader.getLoadedDictionary();
        Set<String> strings = loadedDictionary.keySet();
        CommonUtils.getInstance().sendPluginMessageInServer(player, "&r&l?????????????????????: &7" + String.valueOf(strings));
    }

    @SubCommand("onprocess")
    public void showOnProcess(CommandSender commandSender) {
        Player player = (Player) commandSender;

        HashMap<Location, CookingProcessObject> processPool = HomewardCooking.processPool;
        try {
            Set<Location> strings = processPool.keySet();
            CommonUtils.getInstance().sendPluginMessageInServer(player, "&r&l???????????????????????????: &7");
            for (Location l : strings) {
                CommonUtils.getInstance().sendPluginMessageInServer(player, "&r&l: &7 ?????? - " + l.getWorld().getName() + " &c?????? &7- " + processPool.get(l).getCookingRecipe().getMainOutPut().getMaterial());
            }
        } catch (Exception e) {
            CommonUtils.getInstance().sendPluginMessageInServer(player, "&6&l?????????????????????????????????");
        }


    }


    @Default
    public void defaultCommand(final CommandSender commandSender) {


    }
}
