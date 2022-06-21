
package homeward.plugin.homewardcooking.commands;

import de.tr7zw.changeme.nbtapi.NBTItem;
import homeward.plugin.homewardcooking.Homewardcooking;
import homeward.plugin.homewardcooking.guis.CookingGUI;
import homeward.plugin.homewardcooking.pojo.CookingPotThing;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Completion;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
//import net.minecraft.network.chat.IChatBaseComponent;
//import net.minecraft.network.protocol.game.PacketPlayOutGameStateChange;
//import net.minecraft.server.level.EntityPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundGameEventPacket;
import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.server.network.PlayerConnection;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

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

    @SubCommand("open")
    public void openCookingInterfaces(CommandSender commandSender) {
        Player player = (Player) commandSender;
        CookingGUI cookingGUI = new CookingGUI();
        cookingGUI.setGuiName("厨艺锅");
        cookingGUI.open(player);

    }

    @SubCommand("checkPool")
    public void checkPool(CommandSender commandSender) {
        Player player = (Player) commandSender;
        player.sendMessage(String.valueOf(Homewardcooking.GUIPools.size()));

    }

    @SubCommand("removePool")
    public void removePool(CommandSender commandSender) {
        Player player = (Player) commandSender;
        Homewardcooking.GUIPools.clear();
        player.sendMessage(String.valueOf(Homewardcooking.GUIPools.size()));

    }

    @SubCommand("give")
    @Completion({"#players",  "#range:1-64"})
    public void giveCookingPot(CommandSender commandSender, final Player player, final Integer number) {


        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6给予 " + player.getDisplayName() + " &6厨艺锅"));
        ItemStack cauldron = CookingPotThing.getVanillaItemStack();
        cauldron.setAmount(number);
        player.getInventory().addItem(cauldron);


    }

    @Default
    public void defaultCommand(final CommandSender commandSender) {


    }
}
