package cn.pomelo.sync;

import java.util.concurrent.TimeUnit;

/**
 * 1. 同一个类，一个方法被锁，不会影响没有上锁方法进入
 * 2. 同一个类，静态方法锁和非静态方法锁互补干扰，静态方法是类级别锁，非静态方法是对象锁
 *
 * @author: zhengyong Date: 2018/9/27 Time: 上午11:48
 */
public class LockOneMethod {

    public synchronized void getOne() {
        System.out.println("one synchronized start");
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("one synchronized end");
    }

    public void getTwo() {
        System.out.println("two finish");
    }

    public synchronized void getThree() {
        System.out.println("three synchronized finish");
    }

    public static synchronized void getFour() {
        System.out.println("four static synchronized finish");
    }
}
