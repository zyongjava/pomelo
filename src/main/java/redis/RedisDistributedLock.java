package redis;

import org.apache.commons.lang3.math.NumberUtils;
import redis.clients.jedis.Jedis;

import java.util.Objects;

/**
 * <pre>https://blog.csdn.net/rich_family/article/details/79034808</pre>
 *
 * @author: zhengyong Date: 2018/7/6 Time: 下午5:32
 */
public class RedisDistributedLock {


    /**
     * 获取锁直到成功
     *
     * @param jedis   redis客户端
     * @param key     锁key
     * @param timeout 锁超时时间
     */
    public void tryLockSuccess(Jedis jedis, String key, long timeout) {
        // 一直循环直到获取到锁
        while (!this.tryLock(jedis, key, timeout)) {
        }
    }

    /**
     * 获取锁
     *
     * @param jedis   redis客户端
     * @param key     锁key
     * @param timeout 锁超时时间
     * @return true / false
     */
    public boolean tryLock(Jedis jedis, String key, long timeout) {
        long currentTime = System.currentTimeMillis();
        String expires = String.valueOf(timeout + currentTime);
        //设置互斥量
        if (jedis.setnx(key, expires) > 0) {
            return true;
        } else {
            // 未获取到锁，判断锁是否超时
            String currentLockTime = jedis.get(key);
            //检查锁是否超时
            if (currentLockTime != null && Long.parseLong(currentLockTime) < currentTime) {
                //获取旧的锁时间并设置互斥量
                String oldLockTime = jedis.getSet(key, expires);
                //旧值与当前时间比较
                if (oldLockTime != null && Objects.equals(oldLockTime, currentLockTime)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 释放锁（如果锁已超时，那么锁可能已由其他线程获得，这时直接执行 DEL lock.id 操作会导致把其他线程已获得的锁释放掉。所以这里只删除未超时的lock.id）
     *
     * @param jedis redis客户端
     * @param key   锁key
     * @return true / false
     */
    public boolean unlock(Jedis jedis, String key) {
        //判断锁是否超时，没有超时才将互斥量删除
        String expire = jedis.get(key);
        long lockExpiresTime = NumberUtils.toLong(expire);
        if (lockExpiresTime > System.currentTimeMillis()) {
            jedis.del(key);
        }
        return true;
    }

}
