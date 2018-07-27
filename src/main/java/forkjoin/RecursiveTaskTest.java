package forkjoin;


import com.alibaba.fastjson.JSON;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ForkJoin 通俗来讲就是把一件事情拆分成若干个递归的小事情，并且框架提供多线程形式并发完成多个小事情后合并结果，完成大事情
 * <p>
 * 有返回值
 */
public class RecursiveTaskTest {

    public static void main(String[] args) throws Exception {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        ForkJoinTask<Integer> forkJoinTask = forkJoinPool.submit(new AddTask(nums));
        Integer sum = forkJoinTask.get();
        forkJoinPool.shutdown();
        System.out.println(sum);
    }


    private static class AddTask extends RecursiveTask<Integer> {

        private List<Integer> nums;

        public AddTask(final List nums) {
            this.nums = nums;
        }

        /**
         * 拆分任务，二分法拆分数组，拆到2个数累加
         */
        @Override
        protected Integer compute() {
            System.out.println(Thread.currentThread().getName() + "_" + JSON.toJSONString(nums));
            int size = nums.size();
            if (size <= 1) {
                return nums.get(0);
            }
            int parts = size / 2;
            List left = nums.subList(0, parts);
            List right = nums.subList(parts, size);
            AddTask leftTask = new AddTask(left);
            AddTask rightTask = new AddTask(right);
            invokeAll(leftTask, rightTask);
            Integer leftValue = leftTask.join();
            Integer rightValue = rightTask.join();
            return leftValue + rightValue;
        }
    }
}