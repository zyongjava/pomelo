package spi;

/**
 * Created by zhengyong on 17/6/2.
 */
public class JavaHello implements HelloInterface {

    @Override
    public void say() {
        System.out.println("my name is java hello");
    }
}
