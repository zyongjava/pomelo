### 一、概述

```
Spring 3.1 引入了激动人心的基于凝视（annotation）的缓存（cache）技术，它本质上不是一个具体的缓存实现方案（比如EHCache 或者 OSCache），而是一个对缓存使用的抽象，通过在既有代码中加入少量它定义的各种 annotation，即能够达到缓存方法的返回对象的效果。

Spring 的缓存技术还具备相当的灵活性。不仅能够使用 SpEL（Spring Expression Language）来定义缓存的 key 和各种 condition，还提供开箱即用的缓存暂时存储方案，也支持和主流的专业缓存比如 EHCache 集成。

其特点总结例如以下：

通过少量的配置 annotation 凝视就可以使得既有代码支持缓存
支持开箱即用 Out-Of-The-Box，即不用安装和部署额外第三方组件就可以使用缓存
支持 Spring Express Language，能使用对象的不论什么属性或者方法来定义缓存的 key 和 condition
支持 AspectJ，并通过事实上现不论什么方法的缓存支持
支持自己定义 key 和自己定义缓存管理者，具有相当的灵活性和扩展性
本文将针对上述特点对 Spring cache 进行具体的介绍，主要通过一个简单的样例和原理介绍展开，然后我们将一起看一个比較实际的缓存样例。最后会介绍 spring cache 的使用限制和注意事项。
```

### 二、曾经我们如何实现缓存
```
public static void main(String[] args) {
	// 1. 先查询缓存数据
	String cacheData = redisClient.get("key");
	// 2. 缓存数据不存在，查询数据库，查询到数据再放入缓存
	if(cacheData == null) {
		cacheData = loadFromDB("key");
		redisClient.put("key", cacheData);
	}
}

public static String loadFromDB(String key) {
	System.out.println("query data from db, key=" + key);
}
```

### 三、利用spring Cache如何实现缓存
```
public static void main(String[] args) {
	// 1. 一句话实现自动保存缓存
	String cacheData = cacheData = loadFromDB("key");
}

@Cacheable(value = "myCache", key = "#p0")
public static String loadFromDB(String key) {
	System.out.println("query data from db, key=" + key);
} 
```

### 四、spring Cache实现简单示列(ConcurrentMap)

```
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-context</artifactId>
    <version>3.2.4.RELEASE</version>
</dependency>
```

##### 1）XML定义

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:cache="http://www.springframework.org/schema/cache" xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/cache http://www.springframework.org/schema/cache/spring-cache.xsd">

    <!-- spring cache config-->
    <cache:annotation-driven/>
    <bean id="userService" class="springcache.UserService"/>
    <bean id="redisCustomCache" class="springcache.RedisCustomCache"/>
    <bean id="cacheManager" class="org.springframework.cache.support.SimpleCacheManager">
        <property name="caches">
            <set>
                <bean class="org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean" p:name="default"></bean>
                <bean class="org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean" p:name="userCache"></bean>
            </set>
        </property>
    </bean>

</beans>

```
##### 2）缓存方法实现

```
package springcache;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.Random;

/**
 * @author: zhengyong Date: 2018/11/23 Time: 上午10:51
 */
public class UserService {

    // key="#p0" (https://stackoverflow.com/questions/29862053/spring-cache-null-key-returned-for-cache-operation)
    @Cacheable(value = "userCache", key = "#p0")
    public UserBO getUserName(String id) {
        return getFromDB(id, null);
    }

    @CachePut(value = "userCache", key = "#p0")
    public UserBO updateUser(String id, String name) {
        return getFromDB(id, name);
    }

    @CacheEvict(value = "userCache", key = "#p0")
    public void deleteUser(String id) {
        System.out.println("delete username from db, id=" + id);
    }

    private UserBO getFromDB(String id, String username) {
        System.out.println("query username from db, id=" + id);
        UserBO user = new UserBO();
        user.setId(id);
        if (StringUtils.isBlank(username)) {
            username = String.format("username-%s", id);
        }
        user.setUsername(username);
        user.setAge(new Random().nextInt() * 100);
        return user;
    }
}

```

UserBO对象：

```
package springcache;

import java.io.Serializable;

/**
 * @author: zhengyong Date: 2018/11/26 Time: 下午4:29
 */
public class UserBO implements Serializable {

    private String id;
    private String username;
    private Integer age;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}

```

##### 3）测试类

```
package springcache;

import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>https://www.ibm.com/developerworks/cn/opensource/os-cn-spring-cache/index.html</p>
 * <p>
 * Created by zhengyong on 17/3/3.
 */
public class SpringCacheTest {

    public static void main(String[] args) {

        UserService userService = (UserService) ctx.getBean("userService");

        System.out.println(" ----  @Cacheable test ----");
        userService.getUserName("st1");
        userService.getUserName("st1");
        userService.getUserName("st1");
        userService.getUserName("st2");
        userService.getUserName("st3");
        userService.getUserName("st4");
        userService.getUserName("st4");

        System.out.println(" \n \n");

        System.out.println(" ----  @CachePut test ----");
        userService.updateUser("st1", null);
        userService.updateUser("st1", null);
        userService.updateUser("st1", null);
        userService.updateUser("st2", null);
        userService.updateUser("st3", null);
        userService.updateUser("st4", null);
        userService.updateUser("st4", null);

        System.out.println(" \n \n");

        System.out.println(" ----  @CacheEvict test ----");
        userService.getUserName("st1");
        userService.deleteUser("st1");
        userService.getUserName("st1");

    }
}

```

##### 4）运行结果

```
 ----  @Cacheable test ----
query username from db, id=user1
put data into redis, key=user1; value={"age":-349682088,"id":"user1","username":"username-user1"}
get data from redis, key=user1; value={"age":-349682088,"id":"user1","username":"username-user1"}
get data from redis, key=user1; value={"age":-349682088,"id":"user1","username":"username-user1"}
query username from db, id=user2
put data into redis, key=user2; value={"age":-565258724,"id":"user2","username":"username-user2"}
query username from db, id=user3
put data into redis, key=user3; value={"age":1926641124,"id":"user3","username":"username-user3"}
query username from db, id=user4
put data into redis, key=user4; value={"age":-784056400,"id":"user4","username":"username-user4"}
get data from redis, key=user4; value={"age":-784056400,"id":"user4","username":"username-user4"}
 
 ----  @CachePut test ----
query username from db, id=user1
put data into redis, key=user1; value={"age":-538300200,"id":"user1","username":"name1_1"}
query username from db, id=user1
put data into redis, key=user1; value={"age":178807824,"id":"user1","username":"name2_2"}
query username from db, id=user1
put data into redis, key=user1; value={"age":1506519876,"id":"user1","username":"name3_3"}
query username from db, id=user2
put data into redis, key=user2; value={"age":-1707019128,"id":"user2","username":"name4_4"}
query username from db, id=user3
put data into redis, key=user3; value={"age":-686759988,"id":"user3","username":"name5_5"}
query username from db, id=user4
put data into redis, key=user4; value={"age":-1791199832,"id":"user4","username":"name6_6"}
query username from db, id=user4
put data into redis, key=user4; value={"age":-1858946916,"id":"user4","username":"name7_7"}
 
 ----  @CacheEvict test ----
get data from redis, key=user1; value={"age":1506519876,"id":"user1","username":"name3_3"}
delete username from db, id=user1
query username from db, id=user1
put data into redis, key=user1; value={"age":904758244,"id":"user1","username":"username-user1"}
```

### 五、spring Cache高级扩展(Redis)
1) 自定义cache实现，实现接口`org.springframework.cache.Cache`

```
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
       // TODO
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

```

1.1）HessianUtil 序列化工具类实现：

```
package serialization.hessian;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

/**
 * <p>
 * hessian2 序列化和反序列化
 * </p>
 * Created by zhengyong on 17/5/27.
 */
public class HessianUtil {

    /**
     * 序列化数据
     * 
     * @param message
     * @throws Exception
     */
    public static byte[] encoder(Object message) throws Exception {

        ByteArrayOutputStream byteOutputStream = null;
        // hessian解析二进制
        Hessian2Output hessian2Output = null;
        try {
            byteOutputStream = new ByteArrayOutputStream();
            hessian2Output = new Hessian2Output(byteOutputStream);
            /**
             * 设置serializerFactory能将hessian序列化的效率提高几倍，如果不设置会导致最初的几次序列化效率低，出现阻塞的情况。
             * 主要原因是如果hessian2Output中的serializerFactory为空的话，writeObject的时候创建这个对象的时候会出现阻塞，导致最初几次调用耗时过长
             */
            SerializerFactory factory = new SerializerFactory();
            hessian2Output.setSerializerFactory(factory);
            // 写入序列化信息
            // hessian2Output.startMessage();
            hessian2Output.writeObject(message);
            // hessian2Output.completeMessage();

            hessian2Output.flush(); // 将序列化信息发送出去

            byte[] data = byteOutputStream.toByteArray();

            return data;
        } finally {
            if (byteOutputStream != null) {
                byteOutputStream.close();
            }
            if (hessian2Output != null) {
                hessian2Output.close();
            }
        }
    }

    /**
     * 反序列化二进制数据
     * 
     * @param data
     * @return Object
     * @throws Exception
     */
    public static Object decoder(byte[] data) throws Exception {

        ByteArrayInputStream is = null;
        // hessian解析二进制
        Hessian2Input hessian2Input = null;
        try {
            is = new ByteArrayInputStream(data);
            hessian2Input = new Hessian2Input(is);
            /**
             * 设置serializerFactory能将hessian序列化的效率提高几倍，如果不设置会导致最初的几次序列化效率低，出现阻塞的情况
             */
            SerializerFactory factory = new SerializerFactory();
            hessian2Input.setSerializerFactory(factory);
            // hessian反序列化对象
            //hessian2Input.startMessage();
            Object ret = hessian2Input.readObject();
            //hessian2Input.completeMessage();
            return ret;
        } finally {
            if (is != null) {
                is.close();
            }
            if (hessian2Input != null) {
                hessian2Input.close();
            }
        }
    }

}

```
2) XML修改为如下

```
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:cache="http://www.springframework.org/schema/cache" xmlns:p="http://www.springframework.org/schema/p"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/cache http://www.springframework.org/schema/cache/spring-cache.xsd">

    <!-- spring cache config-->
    <cache:annotation-driven/>
    <bean id="userService" class="springcache.UserService"/>
    <bean id="redisCustomCache" class="springcache.RedisCustomCache"/>
    <bean id="jedisBean" class="redis.clients.jedis.Jedis" >
        <constructor-arg name="host" value="127.0.0.1" />
        <constructor-arg name="port" value="6379" />
        <constructor-arg name="timeout" value="100000" />
    </bean>
    <bean id="cacheManager" class="org.springframework.cache.support.SimpleCacheManager">
        <property name="caches">
            <set>
                <bean class="org.springframework.cache.concurrent.ConcurrentMapCacheFactoryBean" p:name="default"></bean>
                <!-- 自定义redis cache实现 -->
                <bean class="springcache.RedisCustomCache">
                    <property name="jedis" ref="jedisBean"></property>
                    <property name="name" value="userCache"></property>
                </bean>
            </set>
        </property>
    </bean>

</beans>

```

##### 3）运行结果

```
 ----  @Cacheable test ----
query username from db, id=st1
put data into redis, key=st1; value="username-st1"
get data from redis, key=st1; value="username-st1"
get data from redis, key=st1; value="username-st1"
query username from db, id=st2
put data into redis, key=st2; value="username-st2"
put data into redis, key=st3; value="username-st3"
query username from db, id=st4
put data into redis, key=st4; value="username-st4"
query username from db, id=st4
get data from redis, key=st4; value="username-st4"
 
 ----  @CachePut test ----
query username from db, id=st1
put data into redis, key=st1; value="username-st1"
query username from db, id=st1
put data into redis, key=st1; value="username-st1"
query username from db, id=st1
put data into redis, key=st1; value="username-st1"
query username from db, id=st2
put data into redis, key=st2; value="username-st2"
query username from db, id=st3
put data into redis, key=st3; value="username-st3"
query username from db, id=st4
put data into redis, key=st4; value="username-st4"
query username from db, id=st4
put data into redis, key=st4; value="username-st4"
 
 ----  @CacheEvict test ----
get data from redis, key=st1; value="username-st1"
delete username from db, id=st1
query username from db, id=st1
put data into redis, key=st1; value="username-st1"
```

### 六、参考地址

1）https://www.ibm.com/developerworks/cn/opensource/os-cn-spring-cache/index.html 


