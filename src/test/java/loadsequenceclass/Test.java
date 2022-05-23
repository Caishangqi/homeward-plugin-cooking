package loadsequenceclass;

public class Test {
    static {
        b = 6; //静态初始化 给予 b初始值
        System.out.println("-------------");
    }

    //声明变量a时指定初始值
    static int a = 5;
    static int b = 9;
    static int c;
    static final String compileConstants = "全局宏变量";

    public static void main(String[] args) {
        System.out.println(Test.compileConstants);
    }
}
