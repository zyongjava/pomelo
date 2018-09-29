package design.pattern.singleton;

/**
 * @author: zhengyong Date: 2018/9/29 Time: 下午3:26
 */
public class Test {

    public static void main(String[] args) {
        Singleton1 singleton1 = Singleton1.getInstance();
        Singleton2 singleton2 = Singleton2.getInstance();
        System.out.println("init singleton");
    }
}
