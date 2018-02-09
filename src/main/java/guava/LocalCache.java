package guava;

import com.google.common.cache.*;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

public class LocalCache {

    private static LoadingCache<String, List<String>> caches;

    public static void main(String[] args) throws Exception {
        initCache();

        for (int i = 0; i< 10; i++) {
            getValue("1");
            Thread.sleep(200);
        }
        Thread.sleep(10000);
    }

    public static void initCache() {

        CacheLoader<String, List<String>> loader = initCacheLoader();
        RemovalListener<String, List<String>> removalListener = new RemovalListener<String, List<String>>() {
            @Override
            public void onRemoval(RemovalNotification<String, List<String>> removal) {
                System.out.println("remove cache key:" + removal.getKey());
            }
        };

        caches = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(1, TimeUnit.SECONDS)
                .expireAfterWrite(1, TimeUnit.SECONDS)
                // key remove listener
                .removalListener(removalListener)
                .build(loader);

    }


    private static CacheLoader initCacheLoader() {
        CacheLoader<String, List<String>> loader = new CacheLoader<String, List<String>>() {

            @Override
            public List<String> load(String key) {
                return remoteLoadData(key);
            }

            @Override
            public ListenableFuture<List<String>> reload(final String key, List<String> datas) {
                ListenableFutureTask<List<String>> task = ListenableFutureTask.create(new Callable<List<String>>() {

                    @Override
                    public List<String> call() {
                        return remoteLoadData(key);
                    }
                });
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(task);
                return task;

            }
        };
        CacheLoader.asyncReloading(loader, Executors.newSingleThreadExecutor());
        return loader;
    }

    private static List<String> getValue(String key) {
        long start = System.currentTimeMillis();
        List<String> list = null;
        try {
            list =  caches.get(key);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("get key=%s, cost=%s ms ", key, end-start));
        return list;
    }

    /**
     *
     * @param key
     * @return
     */
    private static List<String> remoteLoadData(String key) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(key + "_" + String.valueOf(new Date().getTime()));
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(String.format("the key=%s get data from remove", key));
        return list;
    }

}
