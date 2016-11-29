package cn.pomelo;

public class addThread implements Runnable{
	int a,b;
	
	public addThread(int a ,int b){
		this.a = a;
		this.b =b;
	}

	@Override
	public void run() {
		synchronized (Integer.valueOf(a)) {
			synchronized (Integer.valueOf(b)) {
				try {
					Thread.sleep(100000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.err.println(a+b);
			}
		}
	}

	public static void main(String[] args) {
		for(int i=0;i<100;i++){
			new Thread(new addThread(1, 2)).start();
			new Thread(new addThread(2, 1)).start();
		}
		
		while(true){
			
		}
	}
}
