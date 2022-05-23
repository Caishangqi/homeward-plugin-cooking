package clazz;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class A {
    private int number1 = 10;


    public String AMethod(String placeholder) {
        System.out.println(placeholder);
        return "返回";
    }
}

public class ClassTest {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException, NoSuchFieldException {

        Class<?> aClass = Class.forName("clazz.A");
        Constructor<?> declaredConstructor = aClass.getDeclaredConstructor();

        Field number1 = aClass.getDeclaredField("number1");
        number1.setAccessible(true);


        Method aMethod = aClass.getMethod("AMethod", String.class);

        Object o = declaredConstructor.newInstance();
        System.out.println(number1.get(o));
        Object pp = aMethod.invoke(o, "pp");
        System.out.println(pp);


    }
}
