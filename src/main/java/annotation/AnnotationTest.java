package annotation;

import java.lang.reflect.Field;

/**
 * annotation 测试类<br/>
 * Created by zhengyong on 16/12/7.
 */
public class AnnotationTest {

    public static void main(String[] args) {
        Field[] fields = UserDTO.class.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            AnnotationConfig annotation = field.getAnnotation(AnnotationConfig.class);
            if (annotation == null) {
                return;
            }
            boolean notNull = annotation.notNull();
            String description = annotation.description();
            String max = annotation.max();
            String min = annotation.min();
            System.out.println(String.format("fieldName=%s,description=%s,max=%s,min=%s,notNull=%s", fieldName,
                                             description, min, max, notNull));

        }
    }

}
