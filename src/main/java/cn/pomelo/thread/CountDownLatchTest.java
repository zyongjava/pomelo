package cn.pomelo.thread;

import com.alibaba.fastjson.JSON;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by pomelo on 16/8/22.
 */
public class CountDownLatchTest {

    public static void main(String[] args) {
        System.out.println("main start.");
        CountDownLatch countDownLatch = new CountDownLatch(3);
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        ConcurrentHashMap<String, Object> resultMap = new ConcurrentHashMap<>();
        executorService.execute(new TaskService(countDownLatch, resultMap, "first"));
        executorService.execute(new TaskService(countDownLatch, resultMap, "second"));
        executorService.execute(new TaskService(countDownLatch, resultMap, "third"));
        try {
            countDownLatch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();

        System.out.println("main finish.");
        JSON.parseObject("",CountDownLatch.class);

        for (String key : resultMap.keySet()) {
            System.out.println(String.format("key=%s; value=%s", key, resultMap.get(key)));
        }
    }

    static class TaskService implements Runnable {

        private CountDownLatch                    countDownLatch;

        private ConcurrentHashMap<String, Object> resultMap;

        private String                            param;

        public TaskService(CountDownLatch countDownLatch, ConcurrentHashMap<String, Object> resultMap, String param){
            this.countDownLatch = countDownLatch;
            this.resultMap = resultMap;
            this.param = param;
        }

        @Override
        public void run() {
            try {
                System.out.println("execute task begin.");
                resultMap.put(param, UUID.randomUUID().toString());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("task finish.");
            } finally {
                System.out.println(String.format("the count down param is %s.", param));
                countDownLatch.countDown();
            }
        }
    }
}
