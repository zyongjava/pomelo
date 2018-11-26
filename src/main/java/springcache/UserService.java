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
