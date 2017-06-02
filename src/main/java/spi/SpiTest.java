package spi;

import java.util.ServiceLoader;

/**
 * <p>
 * spi服务
 * </p>
 * <href>http://www.jianshu.com/p/46aa69643c97</href> Created by zhengyong on 17/6/2.
 */
public class SpiTest {

    public static void main(String[] args) {

        ServiceLoader<HelloInterface> loaders = ServiceLoader.load(HelloInterface.class);
        for (HelloInterface loader : loaders) {
            loader.say();
        }
    }
}
