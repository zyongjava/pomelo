package cn.pomelo;

import java.util.Random;

/**
 * Created by zhengyong on 17/4/19.
 */
public class WhileTest {

    public static void main(String[] args) {
        System.out.println(test()) ;
    }

    public static int test() {
        int i = 0;
        while (true) {
            if (bo()) {
                System.out.println("bo");
                return -1;
            }
            int k = 2;
            if(k == 2) {
                System.out.println("k");
                break;
            }

        }
        System.out.println("finish");
        return 0;
    }

    public static boolean bo() {
        return false;
    }
}
