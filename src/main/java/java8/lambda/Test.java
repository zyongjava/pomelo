package java8.lambda;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: zhengyong Date: 2018/11/1 Time: 上午10:49
 */
public class Test {

    public static void main(String[] args) {

        //Runnable是一个函数接口，只包含了有个无参数的，返回void的run方法；
        //所以lambda表达式左边没有参数，右边也没有return，只是单纯的打印一句话
        new Thread(() -> System.out.println("In Java8!")).start();

        //In Java 8:
        List features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");
        features.forEach(n -> System.out.println(n));
        features.forEach(System.out::println);

        System.out.println("start print L ");
        features.stream().filter(f -> f.toString().startsWith("L")).forEach(System.out::println);
        List list = (List) features.stream().filter(f -> f.toString().startsWith("L")).collect(Collectors.toCollection(ArrayList::new));
    }
}
