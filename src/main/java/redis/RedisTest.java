package redis;

import redis.clients.jedis.Jedis;

/**
 * @author: zhengyong Date: 2018/7/6 Time: 下午5:56
 */
public class RedisTest {
    /**
     * redis ip
     */
    private static final String redisHost = "127.0.0.1";
    /**
     * redis 端口
     */
    private static final int redisPort = 6379;
    /**
     * redis读取超时时间
     */
    private static final int redisTimeout = 1000000;
    /**
     * 锁超时2秒
     */
    private static final long lockTimeout = 2000;

    private static RedisDistributedLock lock = new RedisDistributedLock();

    private static int count = 0;

    public static void main(String[] args) throws InterruptedException {
        // 多线程测试分布式锁
        for (int i = 0; i < 100; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Jedis jedis = new Jedis(redisHost, redisPort, redisTimeout);
                    execute(jedis);
                }
            }).start();
        }
        Thread.sleep(3000);
        System.out.println(count);
    }

    private static void execute(Jedis jedis) {
        String key = "execute";
        // 获取分布式锁
        lock.tryLockSuccess(jedis, key, lockTimeout);
        try {
            count++;
        } finally {
            lock.unlock(jedis, key);
        }
    }
}
