package java8.lambda;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

/**
 * java 8 lambada 基础操作（http://www.runoob.com/java/java8-streams.html）
 *
 * @author: zhengyong Date: 2018/11/1 Time: 上午10:49
 */
public class Test {

    public static void main(String[] args) {

        //Runnable是一个函数接口，只包含了有个无参数的，返回void的run方法；
        //所以lambda表达式左边没有参数，右边也没有return，只是单纯的打印一句话
        new Thread(() -> System.out.println("Test Java8 lambda!")).start();

        //In Java 8:
        List<String> features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API");

        // 1. forEach: 迭代流中的每个数据
        System.out.println("\n... 1. start forEach");
        features.forEach(n -> System.out.println(n));

        System.out.println("\n... 2. start stream ");
        System.out.println("... 3. start map");
        // 2. stream: 为集合创建串行流
        // 3. map: 映射每个元素到对应的结果
        features.stream().map((s) -> s + "***").forEach(System.out::println);

        // 4. filter: 用于通过设置的条件过滤出元素
        System.out.println("\n... 4. start filter");
        features.stream().filter(f -> f.startsWith("L")).forEach(System.out::println);

        // 5. limit: 用于获取指定数量的流
        System.out.println("\n... 5. start limit");
        features.stream().limit(2).forEach(System.out::println);

        // 6. sorted: 用于对流进行排序
        System.out.println("\n... 6. start sorted");
        features.stream().sorted().forEach(System.out::println);

        // 7. parallel: 为集合创建并行流
        System.out.println("\n... 7. start parallel stream");
        features.parallelStream().filter(f -> f.startsWith("L")).forEach(System.out::println);

        // 8.Collectors :实现了很多归约操作
        System.out.println("\n... 8. start Collectors");
        List<String> list = features.stream().filter(f -> f.startsWith("L")).collect(Collectors.toList());
        String str = features.stream().filter(f -> !f.isEmpty()).collect(Collectors.joining(","));
        System.out.println("Collectors list=" + list);
        System.out.println("Collectors str=" + str);

        // 9. summaryStatistics: 统计结果的收集器也非常有用。它们主要用于int、double、long等基本类型上
        System.out.println("\n... 8. start summaryStatistics");
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        IntSummaryStatistics stats = numbers.stream().mapToInt(x -> x).summaryStatistics();
        System.out.println("列表中最大的数 : " + stats.getMax());
        System.out.println("列表中最小的数 : " + stats.getMin());
        System.out.println("所有数之和 : " + stats.getSum());
        System.out.println("平均数 : " + stats.getAverage());
    }
}
