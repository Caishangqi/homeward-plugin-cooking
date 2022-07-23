package homeward.plugin.homewardcooking.utils;

import homeward.plugin.homewardcooking.pojo.CommonMaterial;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class StreamItemsUtils {

    //编译OBJ序列化
    public static String writeEncodedObject(Object object) {

        String encodeObject = null;

        try {

            ByteArrayOutputStream io = new ByteArrayOutputStream();

            BukkitObjectOutputStream os = new BukkitObjectOutputStream(io);


            os.writeObject(object);
            os.flush();

            byte[] serialized = io.toByteArray();
            encodeObject = Base64.getEncoder().encodeToString(serialized);

            os.close();
            io.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return encodeObject;
    }

    //编译OBJ反序列化
    public static Object writeDecodedObject(String stringObject) throws IOException, ClassNotFoundException {

        if (stringObject == null || stringObject.isEmpty()) {
            return CommonMaterial.AIR.getItemStack();
        }

        byte[] serialized = Base64.getDecoder().decode(stringObject);

        ByteArrayInputStream in = new ByteArrayInputStream(serialized);

        BukkitObjectInputStream bin = new BukkitObjectInputStream(in);

        Object o = bin.readObject();
        bin.close();
        in.close();
        return o;

    }

}
