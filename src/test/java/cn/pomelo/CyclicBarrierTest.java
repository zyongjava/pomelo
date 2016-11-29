package cn.pomelo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CyclicBarrierTest {
	public static void main(String[] args) {
		ExecutorService service = Executors.newCachedThreadPool();
		final CyclicBarrier cb = new CyclicBarrier(3); // 三个线程同时到达
		for (int i = 0; i < 3; i++) {
			Runnable runnable = new Runnable() {
				public void run() {
					try {
						Thread.sleep((long) (Math.random() * 10000));
						System.out.println(
								"线程" + Thread.currentThread().getName() + "即将到达集合地点1，当前已有" + (cb.getNumberWaiting() + 1)
										+ "个已到达" + (cb.getNumberWaiting() == 2 ? "都到齐了，继续走啊" : "正在等候"));
						try {
							cb.await();
						} catch (BrokenBarrierException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Thread.sleep((long) (Math.random() * 10000));
						System.out.println(
								"线程" + Thread.currentThread().getName() + "即将到达集合地点2，当前已有" + (cb.getNumberWaiting() + 1)
										+ "个已到达" + (cb.getNumberWaiting() == 2 ? "都到齐了，继续走啊" : "正在等候"));
						try {
							cb.await();
						} catch (BrokenBarrierException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Thread.sleep((long) (Math.random() * 10000));
						System.out.println(
								"线程" + Thread.currentThread().getName() + "即将到达集合地点3，当前已有" + (cb.getNumberWaiting() + 1)
										+ "个已到达" + (cb.getNumberWaiting() == 2 ? "都到齐了，继续走啊" : "正在等候"));
						try {
							cb.await();
						} catch (BrokenBarrierException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			service.execute(runnable);
		}
		service.shutdown();
	}
}