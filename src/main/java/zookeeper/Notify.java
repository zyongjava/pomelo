package zookeeper;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.Charset;
import java.util.Map;

/**
 * Created by pomelo on 16/11/24.
 */
public class Notify {

    private static final int    sessionTimeout = 15000;
    private static final String ZK_HOST        = "192.168.6.55:2181";
    private static final String ZK_PATH        = "/zkPath";

    public static void main(String[] args) throws Exception {

        ZooKeeper zookeeper = new ZooKeeper(ZK_HOST, sessionTimeout, null);
        ZKPaths.mkdirs(zookeeper, ZK_PATH);

        CuratorFramework localCuratorClient = CuratorFrameworkFactory.builder().connectString(ZK_HOST) // 这里zk请自行替换
                                                                     .sessionTimeoutMs(sessionTimeout).retryPolicy(new ExponentialBackoffRetry(1000,
                                                                                                                                               10,
                                                                                                                                               5000)).build();
        localCuratorClient.start();

        final Map<String, Object> data = Maps.newHashMap();
        data.put("version", System.currentTimeMillis());
        data.put("data", "我想删除第一条数据");// 放入业务数据

        final Stat stat = new Stat();
        final byte[] oldData = localCuratorClient.getData().storingStatIn(stat).forPath(ZK_PATH); // 先读区旧数据
        System.out.println(String.format("oldData: %s ", new String(oldData, Charset.forName("UTF-8"))));
        System.out.println(String.format("newData: %s", JSON.toJSONString(data)));

        // 若不需要处理旧数据就直接往下
        byte[] newData = JSON.toJSONString(data).getBytes(Charset.forName("UTF-8"));
        // 防止并发问题
        final Stat newStat = localCuratorClient.setData().withVersion(stat.getVersion()).forPath(ZK_PATH, newData);
        System.out.println(String.format("write data result :%s", JSON.toJSONString(newStat)));

        Thread.sleep(1000000);
    }

}
