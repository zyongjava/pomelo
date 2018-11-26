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
