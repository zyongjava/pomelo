package ipaddress;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * http://aokunsang.iteye.com/blog/622498
 * </p>
 * Created by zhengyong on 17/4/18.
 */
public class IpAddressTest {

    // 6M 内存,存放字符串ip大约共31200条
    // 6M 内存,存放long类型ip大约共144919
    private static List ipSet = new ArrayList<>(150000);

    /**
     *  一个long类型占8个字节
     *  100000000条ip地址需要内存800
     *  100000000 * 8 / 1000 / 1000 = 800M * 43 = 30G
     * @param args
     */
    public static void main(String[] args) {
        int i=0;
        java.util.Random r =new java.util.Random(254);
        StringBuilder builder = new StringBuilder(100);
        while(true) {

            builder.append(r.nextInt()).append(".").append(r.nextInt()).append(".").append(r.nextInt()).append(".").append(r.nextInt());
            i++;
            String ip = builder.toString();
            long ipLong = ipToLong(ip);
            ipSet.add(ip);
            System.out.println(i);
            builder.delete(0,builder.length());
        }

    }

    public static long ipToLong(String ipString) {
        long result = 0;
        java.util.StringTokenizer token = new java.util.StringTokenizer(ipString, ".");
        result += Long.parseLong(token.nextToken()) << 24;
        result += Long.parseLong(token.nextToken()) << 16;
        result += Long.parseLong(token.nextToken()) << 8;
        result += Long.parseLong(token.nextToken());
        return result;
    }

    public static String longToIp(long ipLong) {
        StringBuilder sb = new StringBuilder();
        sb.append(ipLong >>> 24);
        sb.append(".");
        sb.append(String.valueOf((ipLong & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(String.valueOf((ipLong & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append(String.valueOf(ipLong & 0x000000FF));
        return sb.toString();
    }

}
