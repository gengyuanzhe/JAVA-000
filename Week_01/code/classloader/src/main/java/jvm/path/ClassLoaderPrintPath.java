package jvm.path;

import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;

import sun.misc.Launcher;

/**
 * 打印各个classloader加载的jar包
 *
 * @author gengyuanzhe
 * @since 2020-10-18
 */
public class ClassLoaderPrintPath {
    public static void main(String[] args) {
        //启动类加载器
        System.out.println("启动类加载器");
        URL[] urls = Launcher.getBootstrapClassPath().getURLs();
        System.out.println(Arrays.deepToString(urls).replace(",", "\n"));


        // 扩展类加载器
        System.out.println("扩展类加载器");
        printURLForClassLoader(ClassLoaderPrintPath.class.getClassLoader().getParent());

        // 应用类加载器
        System.out.println("应用类加载器");
        printURLForClassLoader(ClassLoaderPrintPath.class.getClassLoader());
    }

    private static void printURLForClassLoader(ClassLoader classLoader) {
        Object ucp = insightField(classLoader, "ucp");
        Object path = insightField(ucp, "path");
        ArrayList ps = (ArrayList) path;
        for (Object p : ps) {
            System.out.println(p);
        }
    }


    public static Object insightField(Object obj, String fieldName) {
        Field field = null;
        try {
            if (obj instanceof URLClassLoader) {
                field = URLClassLoader.class.getDeclaredField(fieldName);
            } else {
                field = obj.getClass().getDeclaredField(fieldName);
            }
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}
