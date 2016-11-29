package cn.pomelo;

public class NotifyTest {
	private String flag[] = { "true" };

	class NotifyThread extends Thread {
		public NotifyThread(String name) {
			super(name);
		}

		public void run() {
			try {
				sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized (flag) {
				flag[0] = "false";
				System.out.println("222222");
				flag.notifyAll();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("3333333");
			}
			System.out.println("44444");
		}
	};

	class WaitThread extends Thread {
		public WaitThread(String name) {
			super(name);
		}

		public void run() {
			synchronized (flag) {
				while (flag[0] != "false") {
					System.out.println(getName() + " begin waiting!");
					long waitTime = System.currentTimeMillis();
					try {
						flag.wait();

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					waitTime = System.currentTimeMillis() - waitTime;
					System.out.println("wait time :" + waitTime);
				}
				System.out.println(getName() + " end waiting!");
			}
		}
	}

	public static void main(String[] args) throws InterruptedException {
		System.out.println("Main Thread Run!");
		NotifyTest test = new NotifyTest();
		NotifyThread notifyThread = test.new NotifyThread("notify01");
		WaitThread waitThread01 = test.new WaitThread("waiter01");
		WaitThread waitThread02 = test.new WaitThread("waiter02");
		WaitThread waitThread03 = test.new WaitThread("waiter03");
		notifyThread.start();
		waitThread01.start();
		waitThread02.start();
		waitThread03.start();
	}

}