package loadsequenceclass;

import java.net.URL;

public class LoadClassProp {

    public static void main(String[] args) {

        ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
        URL resource = systemClassLoader.getResource("");
        System.out.println(resource);

        ClassLoader extensionClassLoader = systemClassLoader.getParent();
        System.out.println(extensionClassLoader);
        System.out.println(System.getProperty("java.ext.dirs"));

        System.out.println(extensionClassLoader.getParent());

    }

}
