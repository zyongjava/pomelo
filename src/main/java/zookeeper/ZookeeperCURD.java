package zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;

import java.nio.charset.Charset;

/**
 * zookeeper 基本操作<br/>
 * Created by pomelo on 16/11/24.
 */
public class ZookeeperCURD {

    private static final int        sessionTimeout = 15000;
    private static final String     ZK_HOST        = "192.168.6.55:2181";
    private static final String     ZK_PATH        = "/zkPath";
    private static CuratorFramework curatorClient  = null;

    public static void main(String[] args) throws Exception {

        createClient();

        create(ZK_PATH);

        setDataValue(ZK_PATH, "test data");

        getDataValue(ZK_PATH);

        setDataValue(ZK_PATH, "test2 data");

        getDataValue(ZK_PATH);

        delete(ZK_PATH);

    }

    /**
     * 创建zookeeper访问客户端
     * 
     * @throws Exception
     */
    private static void createClient() throws Exception {

        if (curatorClient == null) {
            synchronized (ZookeeperCURD.class) {
                curatorClient = CuratorFrameworkFactory.builder().connectString(ZK_HOST).sessionTimeoutMs(sessionTimeout).retryPolicy(new ExponentialBackoffRetry(1000,
                                                                                                                                                                  10,
                                                                                                                                                                  5000)).build();
                curatorClient.start();
            }
        }
    }

    /**
     * 创建zookeeper节点信息
     *
     * @throws Exception
     */
    private static void create(String path) throws Exception {
        ZooKeeper zookeeper = new ZooKeeper(ZK_HOST, sessionTimeout, null);
        ZKPaths.mkdirs(zookeeper, path);
        System.out.println(String.format("create path=%s", path));
    }

    /**
     * 设置path node 值
     * 
     * @param path 路径
     * @param data 值
     * @throws Exception
     */
    private static void setDataValue(String path, String data) throws Exception {
        curatorClient.setData().forPath(path, data.getBytes(Charset.forName("UTf-8")));
        System.out.println(String.format("set path=%s, data=%s", path, data));
    }

    /**
     * 获取path node 值
     * 
     * @param path 路径
     * @return 值
     * @throws Exception
     */
    private static String getDataValue(String path) throws Exception {
        byte[] value = curatorClient.getData().forPath(path);
        String result = new String(value, Charset.forName("UTf-8"));
        System.out.println(String.format("get path=%s, data=%s", path, result));
        return result;

    }

    /**
     * 删除节点
     * 
     * @param path 路径
     */
    private static void delete(String path) throws Exception {
        curatorClient.delete().forPath(path);
        System.out.println(String.format("delete path=%s", path));
    }
}
