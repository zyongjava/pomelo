package java8.time;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * @author: zhengyong Date: 2018/11/2 Time: 上午11:54
 */
public class Test {

    public static void main(String[] args) {
        Clock c1 = Clock.systemUTC(); //系统
        c1.millis();
        Instant instant1 = Instant.now();
        LocalDateTime now = LocalDateTime.now();
    }
}
