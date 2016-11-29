package zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;

import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by pomelo on 16/11/24.
 */
public class Listener {

    private static final int sessionTimeout = 15000;
    private static final String ZK_HOST = "192.168.6.55:2181";
    private static final String ZK_PATH = "/zkPath";

    private static ExecutorService executorService = Executors.newFixedThreadPool(5);

    public static void main(String[] args) throws Exception {

        ZooKeeper zookeeper = new ZooKeeper(ZK_HOST, sessionTimeout, null);
        ZKPaths.mkdirs(zookeeper, ZK_PATH);

        // 创建5个listener
        for (int i = 0; i < 5; i++) {
            final int index = i + 1;
            executorService.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        createListeners("Thread-" + index);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        Thread.sleep(1000000);

    }

    /**
     * 创建listener
     *
     * @param threadName 线程名称
     * @throws Exception
     */
    private static void createListeners(final String threadName) throws Exception {
        CuratorFramework localCuratorClient = CuratorFrameworkFactory.builder().connectString(ZK_HOST).sessionTimeoutMs(sessionTimeout).retryPolicy(new ExponentialBackoffRetry(1000,
                10, 5000)).build();
        localCuratorClient.start();

        final NodeCache nodeCache = new NodeCache(localCuratorClient, ZK_PATH);
        nodeCache.getListenable().addListener(new NodeCacheListener() {

            @Override
            public void nodeChanged() throws Exception {
                final byte[] curr = nodeCache.getCurrentData().getData();// 取到最新的数据

                System.out.println(String.format("%s收到客户端通知: %s", threadName, new String(curr, Charset.forName("UTF-8"))));
                // 业务逻辑
            }
        });
        // 一定要start()
        nodeCache.start();
        System.out.println(String.format("listener %s start", threadName));

        // 系统关闭的时候请调用nodeCache.close();
    }

}
