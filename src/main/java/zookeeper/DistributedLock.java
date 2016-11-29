package zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by pomelo on 16/11/24.
 */
public class DistributedLock {

    private static final int       sessionTimeout  = 15000;
    private static final String    ZK_HOST         = "192.168.6.55:2181";
    private static final String    ZK_PATH         = "/zkPath";
    private static int             count           = 0;

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) throws Exception {

        ZooKeeper zookeeper = new ZooKeeper(ZK_HOST, sessionTimeout, null);
        ZKPaths.mkdirs(zookeeper, ZK_PATH);

        // 创建200个线程
        for (int i = 0; i < 200; i++) {
            final int index = i + 1;
            executorService.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        count("Thread-" + index);
                        Thread.sleep(500);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        executorService.shutdown();
    }

    /**
     * 统计count数
     *
     * @param threadName 线程名称
     * @throws Exception
     */
    private static void count(final String threadName) throws Exception {

        CuratorFramework localCuratorClient = CuratorFrameworkFactory.builder().connectString(ZK_HOST).sessionTimeoutMs(sessionTimeout).retryPolicy(new ExponentialBackoffRetry(1000,
                                                                                                                                                                                10,
                                                                                                                                                                                5000)).build();
        localCuratorClient.start();

        // 分布式锁逻辑
        lock(localCuratorClient, threadName);

        // 系统关闭的时候请调用nodeCache.close();
    }

    /**
     * 分布式锁逻辑
     * 
     * @param curatorClient zkClient
     * @param threadName 线程名称
     * @throws Exception
     */
    private static void lock(CuratorFramework curatorClient, final String threadName) throws Exception {
        InterProcessMutex lock = new InterProcessMutex(curatorClient, ZK_PATH);
        if (lock.acquire(2000, TimeUnit.MILLISECONDS)) {
            try {
                // do some work inside of the critical section here
                count++;
                System.out.println(String.format("threadName %s acquire lock, count = %d", threadName, count));
            } finally {
                lock.release();
            }
        }
    }
}