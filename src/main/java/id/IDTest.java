package id;


/**
 * @author: zhengyong Date: 2018/8/7 Time: 下午3:52
 */
public class IDTest {

    public static void main(String[] args) {

        SnowflakeIdWorker msf = new SnowflakeIdWorker(20, 2);
        ThreadSnowFlake t1 = new ThreadSnowFlake(msf);
        ThreadSnowFlake t2 = new ThreadSnowFlake(msf);

        t1.start();
        t2.start();
    }


    /**
     * @author zcl
     * @date 2017/7/12
     **/
    static class ThreadSnowFlake extends Thread {

        SnowflakeIdWorker msf;

        int cnt = 0;

        public ThreadSnowFlake(SnowflakeIdWorker msf) {
            this.msf = msf;
        }

        public void run() {
            if (msf != null) {
                while (cnt < 10) {
                    System.out.println(Thread.currentThread().getId()+": "+msf.nextId());
                    cnt++;
                }
            }
        }
    }
}
