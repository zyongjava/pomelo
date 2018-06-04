package memory;

import serialization.object.Person;
import sizeof.agent.SizeOfAgent;

/**
 * 计算对象占用java内存
 *
 * 参考：https://www.cnblogs.com/magialmoon/p/3757767.html
 *
 * @author: zhengyong Date: 2018/6/1 Time: 下午3:25
 */
public class CaculatorMemorySize {

    public static void main(String[] args) {
        /**
         * 1. 引入jar包：
         *  <pre>
         *      <dependency>
                     <groupId>com.googlecode.sizeofag</groupId>
                     <artifactId>sizeofag</artifactId>
                     <version>1.0.0</version>
                 </dependency>
         *  </pre>
         * 2. 设置VM option:  -javaagent:/Users/zhengyong/Downloads/sizeofag-1.0.0.jar
         * 3.
         */
        System.out.println("String sizeOf：" + SizeOfAgent.sizeOf(new String("test")));
        System.out.println("String fullSizeOf：" + SizeOfAgent.fullSizeOf(new String("test")));

        System.out.println("Integer sizeOf：" + SizeOfAgent.sizeOf(new Integer(1)));
        System.out.println("Integer fullSizeOf：" + SizeOfAgent.fullSizeOf(new Integer(1)));

        Person person = new Person();
        person.setId(1);
        person.setName("test");
        person.setEmail("111111111111test@qq.com");
        System.out.println("Person sizeOf：" + SizeOfAgent.sizeOf(person));
        System.out.println("Person fullSizeOf：" + SizeOfAgent.fullSizeOf(person));

        System.out.println("B2 sizeOf：" + SizeOfAgent.sizeOf(new B2()));
        System.out.println("B2 fullSizeOf：" + SizeOfAgent.fullSizeOf(new B2()));
    }

    static class B2 {
        int b2a;
        Integer b2b;
    }
}
