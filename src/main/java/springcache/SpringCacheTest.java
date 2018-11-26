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
        userService.getUserName("st1");
        userService.getUserName("st1");
        userService.getUserName("st1");
        userService.getUserName("st2");
        userService.getUserName("st3");
        userService.getUserName("st4");
        userService.getUserName("st4");

        System.out.println(" \n \n");

        System.out.println(" ----  @CachePut test ----");
        userService.updateUser("st1", null);
        userService.updateUser("st1", null);
        userService.updateUser("st1", null);
        userService.updateUser("st2", null);
        userService.updateUser("st3", null);
        userService.updateUser("st4", null);
        userService.updateUser("st4", null);

        System.out.println(" \n \n");

        System.out.println(" ----  @CacheEvict test ----");
        userService.getUserName("st1");
        userService.deleteUser("st1");
        userService.getUserName("st1");

    }
}
