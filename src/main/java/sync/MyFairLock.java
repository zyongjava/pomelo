package sync;

import java.util.concurrent.locks.ReentrantLock;

public class MyFairLock extends Thread {

    private ReentrantLock lock = new ReentrantLock(true);

    public void fairLock() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName() + "正在持有锁");
        } finally {
            System.out.println(Thread.currentThread().getName() + "释放了锁");
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        MyFairLock myFairLock = new MyFairLock();
        Runnable runnable = () -> {
            System.out.println(Thread.currentThread().getName() + "启动");
            myFairLock.fairLock();
        };
        Thread[] thread = new Thread[10];
        for (int i = 0; i < 10; i++) {
            thread[i] = new Thread(runnable);
        }
        for (int i = 0; i < 10; i++) {
            thread[i].start();
        }
    }
}