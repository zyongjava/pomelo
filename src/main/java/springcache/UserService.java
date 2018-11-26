package springcache;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

/**
 * @author: zhengyong Date: 2018/11/23 Time: 上午10:51
 */
public class UserService {

    // key="#p0" (https://stackoverflow.com/questions/29862053/spring-cache-null-key-returned-for-cache-operation)
    @Cacheable(value = "userCache", key = "#p0")
    public String getUserName(String id) {
        return getFromDB(id);
    }

    @CachePut(value = "userCache", key = "#p0")
    public String updateUser(String id, String name) {
        return getFromDB(id);
    }

    @CacheEvict(value = "userCache", key = "#p0")
    public void deleteUser(String id) {
        System.out.println("delete username from db, id=" + id);
    }

    private String getFromDB(String id) {
        System.out.println("query username from db, id=" + id);
        return String.format("username-%s", id);
    }
}
