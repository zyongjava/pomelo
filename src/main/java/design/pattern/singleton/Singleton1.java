package design.pattern.singleton;

/**
 * 懒汉单例模块 - 兼顾线程安全和效率的写法
 *
 * @author: zhengyong Date: 2018/9/29 Time: 下午3:22
 */
public class Singleton1 {

    private static volatile Singleton1 singleton = null;

    private Singleton1() {
    }

    public static Singleton1 getInstance() {
        if (singleton == null) {
            synchronized (Singleton1.class) {
                if (singleton == null) {
                    singleton = new Singleton1();
                }
            }
        }
        return singleton;
    }
}
