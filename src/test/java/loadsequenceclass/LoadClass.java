package loadsequenceclass;

class Tester {
    static {
        System.out.println("Tester的类的静态初始化块...");
    }
}

public class LoadClass {
    public static void main(String[] args) throws ClassNotFoundException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        classLoader.loadClass("loadsequenceclass.Tester");
        System.out.println("系统加载Tester类");
        Class.forName("loadsequenceclass.Tester");
    }
}