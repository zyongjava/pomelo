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

        String log4j = SpringCacheTest.class.getResource("/log4j.xml").getPath();
        DOMConfigurator.configure(log4j);// 加载.xml文件

        ApplicationContext ctx = new ClassPathXmlApplicationContext("/applicationContext.xml");
        UserService userService = (UserService) ctx.getBean("userService");

        System.out.println(" ----  @Cacheable test ----");
        userService.getUserName("user1");
        userService.getUserName("user1");
        userService.getUserName("user1");
        userService.getUserName("user2");
        userService.getUserName("user3");
        userService.getUserName("user4");
        userService.getUserName("user4");

        System.out.println(" \n \n");

        System.out.println(" ----  @CachePut test ----");
        userService.updateUser("user1", "name1_1");
        userService.updateUser("user1", "name2_2");
        userService.updateUser("user1", "name3_3");
        userService.updateUser("user2", "name4_4");
        userService.updateUser("user3", "name5_5");
        userService.updateUser("user4", "name6_6");
        userService.updateUser("user4", "name7_7");

        System.out.println(" \n \n");

        System.out.println(" ----  @CacheEvict test ----");
        userService.getUserName("user1");
        userService.deleteUser("user1");
        userService.getUserName("user1");

    }
}
