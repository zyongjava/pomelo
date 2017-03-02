package kafka.model;

/**
 * Created by zhengyong on 17/3/2.
 */
public class User {

    private String  name;

    private Integer age;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
