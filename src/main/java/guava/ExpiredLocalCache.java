package guava;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * totalWeight > maxSegmentWeight = maxWeight / segmentCount + 1 (如果分段超出容量，则执行驱逐。如果最新的条目超过其自身的最大权重，则避免刷新整个缓存。)
 * <p>
 * 过期数据清除：是在get获取到某个数据正好过期，这时会去清除其他所有的过期数据。
 * 1. 添加数据, 发现数据过期都会清除过期数据
 * 2. 获取数据，如果获取的当前数据过期，就会删除所有过期数据
 *
 * @author: zhengyong Date: 2018/7/4 Time: 下午3:08
 */
public class ExpiredLocalCache {

    public static void main(String[] args) throws InterruptedException, ExecutionException {

        CacheLoader<String, Integer> cacheLoader = new CacheLoader<String, Integer>() {
            @Override
            public ListenableFuture<Integer> reload(String key, Integer oldValue) throws Exception {
               // System.out.println(String.format("reload key=%s, value=%s from db.", key, oldValue));
                return super.reload(key, oldValue);
            }

            // 数据加载，默认返回-1,也可以是查询操作，如从DB查询
            @Override
            public Integer load(String key) throws Exception {
               // System.out.println(String.format("load key=%s from db.", key));
                return -1;
            }
        };
        LoadingCache<String, Integer> cache = CacheBuilder.newBuilder().maximumSize(10) // 最多存放十个数据
                .expireAfterWrite(10, TimeUnit.SECONDS) // 缓存200秒
                .refreshAfterWrite(1, TimeUnit.SECONDS)
                .recordStats() // 开启 记录状态数据功能
                .build(CacheLoader.asyncReloading(cacheLoader, Executors.newFixedThreadPool(3)));

        // 插入十个数据
        for (int i = 3; i < 13; i++) {
            cache.put("key" + i, i);
        }
        // 超过最大容量的，删除最早插入的数据，size正确
        System.out.println("size :" + cache.size()); // 10
        // miss++
        System.out.println(cache.getIfPresent("key2")); // null

        Thread.sleep(5000); // 等待5秒
        cache.put("key1", 1);
        cache.put("key2", 2);
        // key5还没有失效，返回5。缓存中数据为key1，key2，key5-key12. hit++
        System.out.println(cache.getIfPresent("key5")); // 5

        Thread.sleep(5000); // 等待5秒
        // 此时key5-key12已经失效，但是size没有更新
        System.out.println("size :" + cache.size()); // 10
        // key1存在, hit++
        System.out.println(cache.getIfPresent("key1")); // 1
        System.out.println("size :" + cache.size()); // 10
        // 获取key5，发现已经失效，然后刷新缓存，遍历数据，去掉失效的所有数据, miss++
         System.out.println(cache.getIfPresent("key5")); // null

        // 此时只有key1，key2没有失效
        System.out.println("size :" + cache.size()); // 2
        Thread.sleep(5000); // 等待5秒
        System.out.println(cache.getIfPresent("key2")); // null
        System.out.println("size :" + cache.size()); // 2

        // load from db
        cache.get("key5");
    }
}
