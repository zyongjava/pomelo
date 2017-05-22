package cn.pomelo;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by zhengyong on 17/4/1.
 */
public class ThreadTest {

    public static void main(String[] args) {

        List<String> vector = new ArrayList<>();
        VectorReadThread read = new VectorReadThread(vector);
        VectorAddThread add = new VectorAddThread(vector);

        for(int i = 0 ; i< 1000; i++) {
            Thread readThread = new Thread(read);
            readThread.start();
            Thread addThread = new Thread(add);
            addThread.start();
        }
    }

}

class VectorReadThread implements Runnable {

    private List<String> vector;

    public VectorReadThread(List<String> vector){
        this.vector = vector;
    }

    @Override
    public void run() {

        int count = vector.size();
        for (int i =0 ; i< count ;i++) {
            System.out.println("read:" + vector.get(i));
        }
    }
}

class VectorAddThread implements Runnable {

    private List<String> vector;

    public VectorAddThread(List<String> vector){
        this.vector = vector;
    }

    @Override
    public void run() {

        vector.add("*" + System.currentTimeMillis());
        System.out.println("add:" + vector.size());
    }
}
