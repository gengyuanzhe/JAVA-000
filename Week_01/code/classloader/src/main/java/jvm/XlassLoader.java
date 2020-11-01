
package jvm;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 自定义一个 Classloader，加载一个 Hello.xlass 文件，执行 hello 方法，此文件内
 * 容是一个 Hello.class 文件所有字节（x=255-x）处理后的文件
 *
 * @author g00471473
 * @since 2020-10-17
 */
public class XlassLoader extends ClassLoader {
    public static void main(String[] args)
        throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        Class<?> clazz = new XlassLoader().findClass("Hello");
        Object obj = clazz.newInstance();
        Method method = clazz.getMethod("hello");
        method.invoke(obj);
    }

    @Override
    protected Class<?> findClass(String name) {
        try {
            URL path = XlassLoader.class.getClassLoader().getResource("Hello.xlass");
            assert path != null;
            System.out.println(path.toURI());
            byte[] bytes = Files.readAllBytes(Paths.get(path.toURI()));
            reverse(bytes);
            return defineClass(name, bytes, 0, bytes.length);
        } catch (ClassCastException | IOException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void reverse(byte[] bytes) {
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (255 - bytes[i]);
        }
    }
}
