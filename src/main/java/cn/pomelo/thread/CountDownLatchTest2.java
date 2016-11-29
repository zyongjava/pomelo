package cn.pomelo.thread;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by pomelo on 16/8/22.
 */
public class CountDownLatchTest2 {

    public static void main(String[] args) {
        System.out.println("main start.");
        CountDownLatch countDownLatch = new CountDownLatch(3);
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        List<String> resultList = Lists.newArrayList();
        executorService.execute(new TaskService(countDownLatch, resultList, "first"));
        try {
            countDownLatch.await(2, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();

        System.out.println("main finish.");

        for (String key : resultList) {
            System.out.println(String.format("value=%s", key));
        }
    }

    static class TaskService implements Runnable {

        private CountDownLatch countDownLatch;

        private List<String>   list;

        private String         param;

        public TaskService(CountDownLatch countDownLatch, List list, String param){
            this.countDownLatch = countDownLatch;
            this.list = list;
            this.param = param;
        }

        @Override
        public void run() {
            try {
                System.out.println("execute task begin.");
                List listResult = Lists.newArrayList();
                //list.add("1234");
                list = listResult;
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
