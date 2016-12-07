package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhengyong on 16/12/7.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AnnotationConfig {

    String description() default "";

    Class type() default String.class;

    String max() default "";

    String min() default "";

    boolean notNull() default true;
}
