package clazz;

import java.lang.annotation.Annotation;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

@Repeatable(Annos.class)
@interface Anno {
}

@Retention(RetentionPolicy.RUNTIME)
@interface Annos {
    Anno[] value();
}

@SuppressWarnings("unchecked")
@Deprecated
@Anno
@Anno
public class ClassAnnotationTest {
    private ClassAnnotationTest() {

    }

    public ClassAnnotationTest(String papi) {
        System.out.println("有参构造器");
    }

    public void info() {
        System.out.println("无参数info方法");

    }

    public void info(String papi) {
        System.out.println("有参info方法 " + papi);
    }

    class Inner {
    }

    public static void main(String[] args) throws Exception {
        Class<?> aClass = Class.forName("clazz.ClassAnnotationTest");
        //获取全部构造器
        Constructor<?>[] declaredConstructors = aClass.getDeclaredConstructors();
        System.out.println("全部构造器如下：");
        for (Constructor c : declaredConstructors) {
            System.out.println(c);
        }
        //获取全部public构造器
        Constructor<?>[] constructors = aClass.getConstructors();
        System.out.println("public构造器如下：");
        for (Constructor c : constructors) {
            System.out.println(c);
        }
        //获取全部public方法
        Method[] methods = aClass.getMethods();
        System.out.println("全部public方法如下：");
        for (Method method : methods) {
            System.out.println(method);
        }

        //获取指定方法
        System.out.println("带一个字符串的参数方法为：" + aClass.getMethod("info", String.class));
        //获取全部注解
        Annotation[] annotations = aClass.getAnnotations();
        System.out.println("全部注解如下");
        for (Annotation annotation : annotations) {
            System.out.println(annotation);
        }
        System.out.println("该Class元素上的@SupressWarning注解为 " + Arrays.toString(aClass.getAnnotationsByType(SuppressWarnings.class)));
        System.out.println("该Class元素上的@Anno注解为 " + Arrays.toString(aClass.getAnnotationsByType(Anno.class)));
        //获取该Class对象全部內部类
        Class<?>[] declaredClasses = aClass.getDeclaredClasses();
        System.out.println("ClassAnnotationTest的全部内部类如下 ");
        for (Class c : declaredClasses) {
            System.out.println(c);
        }

        //使用Class.forName() 方法加载 该类的Inner 内部类
        Class<?> inner = Class.forName("clazz.ClassAnnotationTest$Inner");
        //通过getDeclaringClass() 访问该类所在的外部类
        System.out.println("外部类为 " + inner.getDeclaringClass());
        System.out.println("包为 " + aClass.getPackage());
        System.out.println("父类为 " + aClass.getSuperclass());
    }
}
