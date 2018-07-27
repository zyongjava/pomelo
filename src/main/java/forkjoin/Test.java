package forkjoin;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ForkJoin 通俗来讲就是把一件事情拆分成若干个递归的小事情，并且框架提供多线程形式并发完成多个小事情后合并结果，完成大事情
 * <p>
 * 测试客户端
 */
public class Test {

    public static void main(String[] args) throws Exception {

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        AtomicInteger sum = new AtomicInteger();
        List nums = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        forkJoinPool.invoke(new AddTask(nums, sum));
        forkJoinPool.shutdown();
        System.out.println(sum);
    }


    private static class AddTask extends RecursiveAction {

        private List nums;
        private AtomicInteger sum;

        public AddTask(final List nums, AtomicInteger sum) {
            this.nums = nums;
            this.sum = sum;
        }

        /**
         * 拆分任务，二分法拆分数组，拆到只有一个元素后再分别累加
         */
        @Override
        protected void compute() {
            System.out.println(Thread.currentThread().getName());
            int size = nums.size();
            if (size > 1) {
                int parts = size / 2;
                List left = nums.subList(0, parts);
                AddTask leftTask = new AddTask(left, sum);
                List right = nums.subList(parts, size);
                AddTask rightTask = new AddTask(right, sum);
                invokeAll(leftTask, rightTask);
            } else {
                if (size == 0) {
                    return;
                }
                sum.addAndGet((Integer) nums.get(0));
            }
        }
    }
}