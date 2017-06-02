package spi;

/**
 * Created by zhengyong on 17/6/2.
 */
public class PhpHello implements HelloInterface {
    @Override
    public void say() {
        System.out.println("my name is php hello");
    }
}
