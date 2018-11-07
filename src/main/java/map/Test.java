package map;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * @author: zhengyong Date: 2018/10/11 Time: 下午2:36
 */
public class Test {

    public static void main(String[] args) {
        testHaspMap();
        testLinkHaspMap();
        System.out.println("finish");
    }

    public static void testHaspMap() {
        HashMap map = new HashMap();
        for (int i=0;  i< 20; i++) {
            map.put(i, i);
        }
    }

    // afterNodeAccess 记录插入顺序
    public static void testLinkHaspMap() {
        LinkedHashMap map = new LinkedHashMap();
        for (int i=0;  i< 20; i++) {
            map.put(i, i);
        }
    }

}
