package schema;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by zhengyong on 17/3/3.
 */
public class SchemaTest {

    public static void main(String[] args) {

        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext.xml");
        Student student1 = (Student) ctx.getBean("student1");
        Student student2 = (Student) ctx.getBean("student2");
        Student student3 = (Student) ctx.getBean("student3");

        System.out.println("name: " + student1.getName() + " age :" + student1.getAge());
        System.out.println("name: " + student2.getName() + " age :" + student2.getAge());
        System.out.println("name: " + student3.getName() + " age :" + student3.getAge());
    }
}
