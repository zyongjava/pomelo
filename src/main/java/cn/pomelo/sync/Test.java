package cn.pomelo.sync;

/**
 * 1. 同一个类，一个方法被锁，不会影响没有上锁方法进入
 * 2. 同一个类，静态方法锁和非静态方法锁互补干扰，静态方法是类级别锁，非静态方法是对象锁
 *
 * @author: zhengyong Date: 2018/9/27 Time: 上午11:51
 */
public class Test {

    public static void main(String[] args) {
        final LockOneMethod lockOneMethod = new LockOneMethod();
        new Thread(new Runnable() {
            @Override
            public void run() {
                lockOneMethod.getOne();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                lockOneMethod.getTwo();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                lockOneMethod.getThree();
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                LockOneMethod.getFour();
            }
        }).start();
    }
}
