package cas;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: zhengyong Date: 2018/8/2 Time: 下午4:05
 */
public class ReentrantLockTest {

    private static ReentrantLock reentrantLock = new ReentrantLock();

    public static void main(String[] args) {
        reentrantLock.lock();
        try {
            System.out.println("lock success");
        } finally {
            reentrantLock.unlock();
        }
        ConcurrentHashMap map = new ConcurrentHashMap();
        CopyOnWriteArrayList list = new CopyOnWriteArrayList();
        list.add("1");
        list.get(0);

        map.put("1", "1");
        map.put("1", "2");
        for (int i=1; i<= 1000;i++) {
            map.put("2", i);
        }

        System.out.println(TimeUnit.SECONDS.toNanos(1));



    }

}


