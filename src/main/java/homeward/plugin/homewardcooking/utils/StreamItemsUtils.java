package homeward.plugin.homewardcooking.utils;

import com.google.common.base.Preconditions;
import com.mojang.serialization.Dynamic;
import homeward.plugin.homewardcooking.pojo.CommonMaterial;
import lombok.SneakyThrows;
import net.minecraft.SharedConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.datafix.fixes.References;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Base64;

public class StreamItemsUtils {

    //编译OBJ序列化
    public static String writeEncodedObject(Object object) {

        String encodeObject = null;

        try (ByteArrayOutputStream io = new ByteArrayOutputStream(); BukkitObjectOutputStream os = new BukkitObjectOutputStream(io)) {


            os.writeObject(object);
            os.flush();

            byte[] serialized = io.toByteArray();
            encodeObject = Base64.getEncoder().encodeToString(serialized);


        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return encodeObject;
    }

    //编译OBJ反序列化
    public static <T> T writeDecodedObject(String stringObject, Class<T> clazz) throws IOException, ClassNotFoundException {

        if (stringObject == null || stringObject.isEmpty()) {
            return null;
        }

        byte[] serialized = Base64.getDecoder().decode(stringObject);

        ByteArrayInputStream in = new ByteArrayInputStream(serialized);

        BukkitObjectInputStream bin = new BukkitObjectInputStream(in);

        T o = (T) bin.readObject();
        bin.close();
        in.close();
        return o;

    }

    @SneakyThrows
    public static byte[] serializeItem(ItemStack item) {
        Preconditions.checkNotNull(item, "null cannot be serialized");
        Preconditions.checkArgument(item.getType() != Material.AIR, "air cannot be serialized");

        Field handle = CraftItemStack.class.getDeclaredField("handle");
        handle.setAccessible(true);
        return serializeNbtToBytes((item instanceof CraftItemStack ? (net.minecraft.world.item.ItemStack) handle.get(item) : CraftItemStack.asNMSCopy(item)).save(new CompoundTag()));

    }

    public static ItemStack deserializeItem(byte[] data) {
        Preconditions.checkNotNull(data, "null cannot be deserialized");
        Preconditions.checkArgument(data.length > 0, "cannot deserialize nothing");

        CompoundTag compound = deserializeNbtFromBytes(data);
        int dataVersion = compound.getInt("DataVersion");
        Dynamic<Tag> converted = DataFixers.getDataFixer().update(References.ITEM_STACK, new Dynamic<Tag>(NbtOps.INSTANCE, compound), dataVersion, getDataVersion());
        return CraftItemStack.asCraftMirror(net.minecraft.world.item.ItemStack.of((CompoundTag) converted.getValue()));
    }

    private static byte[] serializeNbtToBytes(CompoundTag compound) {
        compound.putInt("DataVersion", getDataVersion());
        java.io.ByteArrayOutputStream outputStream = new java.io.ByteArrayOutputStream();
        try {
            net.minecraft.nbt.NbtIo.writeCompressed(compound, outputStream);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return outputStream.toByteArray();
    }

    private static CompoundTag deserializeNbtFromBytes(byte[] data) {
        CompoundTag compound;
        try {
            compound = net.minecraft.nbt.NbtIo.readCompressed(new java.io.ByteArrayInputStream(data));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        int dataVersion = compound.getInt("DataVersion");
        Preconditions.checkArgument(dataVersion <= getDataVersion(), "Newer version! Server downgrades are not supported!");
        return compound;
    }

    public static int getDataVersion() {
        return SharedConstants.getCurrentVersion().getWorldVersion();
    }

    public static byte[] serializeAsBytes(@NotNull Object object) {
        byte[] encodeObject = null;

        try (ByteArrayOutputStream byteStream = new ByteArrayOutputStream(); BukkitObjectOutputStream bukkitStream = new BukkitObjectOutputStream(byteStream)) {
            bukkitStream.writeObject(object);
            bukkitStream.flush();

            byte[] serialized = byteStream.toByteArray();
            encodeObject = Base64.getEncoder().encode(serialized);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encodeObject;
    }

    public static @Nullable Object deserializeBytes(byte[] bytes) {
        if (bytes == null) return null;
        Object decodedObject = null;

        byte[] toBytes = Base64.getDecoder().decode(bytes);

        try (ByteArrayInputStream byteStream = new ByteArrayInputStream(toBytes); BukkitObjectInputStream bukkitStream = new BukkitObjectInputStream(byteStream)) {
            decodedObject = bukkitStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return decodedObject;
    }


}
