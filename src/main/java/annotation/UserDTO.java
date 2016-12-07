package annotation;


/**
 * Created by zhengyong on 16/12/5.
 */

public class UserDTO {

    @AnnotationConfig(description = "姓名", notNull = true)
    private String  name;

    @AnnotationConfig(description = "年龄", min = "1", max = "100")
    private Integer age;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

}
