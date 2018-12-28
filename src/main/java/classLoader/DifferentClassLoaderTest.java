package classLoader;

import java.io.IOException;
import java.io.InputStream;

/**
 * 自定义classLoader加载DifferentClassLoaderTest类，出现2个不同类
 * <pre>对于同一个类，如果没有采用相同的类加载器来加载，在调用的时候，会产生意想不到的结果</pre>
 *
 * <pre>
 *     双亲委派：如果一个类加载器收到了加载某个类的请求,则该类加载器并不会去加载该类,
 *     而是把这个请求委派给父类加载器,每一个层次的类加载器都是如此,因此所有的类加载请求最终都会传送到顶端的启动类加载器;
 *     只有当父类加载器在其搜索范围内无法找到所需的类,并将该结果反馈给子类加载器,子类加载器会尝试去自己加载。
 * </pre>
 */
public class DifferentClassLoaderTest {

    public static void main(String[] args) throws Exception {
        ClassLoader classLoader = new ClassLoader() {
            @Override
            public Class<?> loadClass(String name) throws ClassNotFoundException {
                // class在当前loader找不到的情况下使用使用双亲委派加载；（本示列能找到，所以没有委派）
                String fileName = name.substring(name.lastIndexOf(".") + 1) + ".class";
                InputStream stream = getClass().getResourceAsStream(fileName);
                if (stream == null) {
                    return super.loadClass(name);
                }

                try {
                    byte[] b = new byte[stream.available()];
                    // 将流写入字节数组b中
                    stream.read(b);
                    return defineClass(name, b, 0, b.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return super.loadClass(name);
            }
        };

        System.out.println("\n...... not same classloader start ......");
        System.out.println("Current ClassLoader Name: " + classLoader.getClass().getName());
        ClassLoader customTemp = classLoader.getParent();
        while (customTemp != null) {
            System.out.println("Parent ClassLoader Name: " + customTemp.getClass().getName());
            customTemp = customTemp.getParent();
        }
        Class clazz = classLoader.loadClass("classLoader.DifferentClassLoaderTest");
        Object obj = clazz.newInstance();
        System.out.println(obj.getClass());
        System.out.println(System.identityHashCode(clazz));
        System.out.println(obj instanceof DifferentClassLoaderTest);

        /**
         * 基于上述的问题：如果不是同一个类加载器加载，即时是相同的class文件，也会出现判断不想同的情况，
         * 从而引发一些意想不到的情况，为了保证相同的class文件，在使用的时候，是相同的对象，jvm设计的时候，采用了双亲委派的方式来加载类。
         */
        System.out.println("\n...... same classloader start ......");
        ClassLoader loader = DifferentClassLoaderTest.class.getClassLoader();
        System.out.println("Current ClassLoader Name: " + loader.getClass().getName());
        ClassLoader temp = loader.getParent();
        while (temp != null) {
            System.out.println("Parent ClassLoader Name: " + temp.getClass().getName());
            temp = temp.getParent();
        }
        Class sameC1 = DifferentClassLoaderTest.class.getClassLoader().loadClass("classLoader.DifferentClassLoaderTest");
        Class sameC2 = DifferentClassLoaderTest.class.getClassLoader().loadClass("classLoader.DifferentClassLoaderTest");
        Object sameObj = sameC1.newInstance();
        System.out.println(System.identityHashCode(sameC1));
        System.out.println(System.identityHashCode(sameC2));
        System.out.println(sameObj.getClass());
        System.out.println(sameObj instanceof DifferentClassLoaderTest);


        System.out.println();
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        System.out.println("Thread ClassLoader: " + cl.getClass());


    }
}