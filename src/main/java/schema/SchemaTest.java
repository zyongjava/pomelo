package schema;

import org.apache.log4j.xml.DOMConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * <p>student1,student2测试自定义spring schema</p>
 * <p>studentId,teacherId测试spring 如何解决bean循环依赖</p>
 *
 * Created by zhengyong on 17/3/3.
 */
public class SchemaTest {


    public static void main(String[] args) {

        String log4j = SchemaTest.class.getResource("/log4j.xml").getPath();
        DOMConfigurator.configure(log4j);// 加载.xml文件

        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext.xml");
        Student student1 = (Student) ctx.getBean("student1");
        Student student2 = (Student) ctx.getBean("student2");
        Student student3 = (Student) ctx.getBean("studentId");

        System.out.println("name: " + student1.getName() + " age :" + student1.getAge());
        System.out.println("name: " + student2.getName() + " age :" + student2.getAge());
        System.out.println("name: " + student3.getName() + " age :" + student3.getAge());
    }
}
