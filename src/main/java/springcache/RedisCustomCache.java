package springcache;

import com.alibaba.fastjson.JSON;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import redis.clients.jedis.Jedis;
import serialization.hessian.HessianUtil;

import java.util.Set;

/**
 * <p>spring cache redis实现</p>
 *
 * @author: zhengyong Date: 2018/11/23 Time: 上午11:46
 */
public class RedisCustomCache implements Cache {

    private Jedis jedis;

    private String name;

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.jedis;
    }

    @Override
    public ValueWrapper get(Object key) {
        byte[] keyByte = getKeyByte(key);
        byte[] value = jedis.get(keyByte);
        if (value == null) {
            return null;
        }
        Object object = null;
        try {
            object = HessianUtil.decoder(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("get data from redis, key=" + key + "; value=" + JSON.toJSONString(object));
        ValueWrapper obj = (object != null ? new SimpleValueWrapper(object) : null);
        return obj;
    }

    @Override
    public void put(Object key, Object value) {
        if (value == null) {
            return;
        }
        byte[] keyByte = getKeyByte(key);
        byte[] encoderValue = new byte[0];
        System.out.println("put data into redis, key=" + key + "; value=" + JSON.toJSONString(value));
        try {
            encoderValue = HessianUtil.encoder(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jedis.set(keyByte, encoderValue);
    }

    @Override
    public void evict(Object key) {
        byte[] keyByte = getKeyByte(key);
        jedis.del(keyByte);
    }

    @Override
    public void clear() {
        Set<byte[]> keys = jedis.keys("_".getBytes());
        for (byte[] key : keys) {
            jedis.del(key);
        }
    }

    private byte[] getKeyByte(Object key) {
        return key.toString().getBytes();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }
}
