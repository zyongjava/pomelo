package design.pattern.singleton;

/**
 * 懒汉单例模块 - 静态内部类法
 *
 * @author: zhengyong Date: 2018/9/29 Time: 下午3:24
 */
public class Singleton2 {

    private Singleton2() {
    }

    private static class Holder {
        public static Singleton2 singleton2 = new Singleton2();
    }

    public static Singleton2 getInstance() {
        return Holder.singleton2;
    }

}
