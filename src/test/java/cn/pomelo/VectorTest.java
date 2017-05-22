package cn.pomelo;

import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by zhouwenchao on 2017/3/28.
 */
public class VectorTest {


    public static void main(String[] args) {
        Vector<String> list = new Vector();
        long start = System.currentTimeMillis();
        AddRunnable addRunnable = new AddRunnable(list);
        PrintRunnable printRunnable = new PrintRunnable(list);
        Thread thread = new Thread(addRunnable);
        Thread thread1 = new Thread(printRunnable);
        thread.start();
        thread1.start();
        long end = System.currentTimeMillis();
        System.out.printf("时间：" +(end-start));
    }

}

class AddRunnable implements Runnable{

    private Vector<String> list;

    public Vector<String> getList() {
        return list;
    }

    public void setList(Vector<String> list) {
        this.list = list;
    }

    public AddRunnable(Vector<String> list) {
        this.list = list;
    }

    @Override
    public void run() {
        for(int i =0 ; i<10000; i++){
            list.add("aaaa"+list.size());
        }
    }
}

class PrintRunnable implements Runnable{

    private Vector<String> list;

    public Vector<String> getList() {
        return list;
    }

    public void setList(Vector<String> list) {
        this.list = list;
    }

    public PrintRunnable(Vector<String> list) {
        this.list = list;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if(list.size()>0){
            //list=Collections.synchronizedList(list);
            for(int i =0 ; i<10000; i++) {
                for(String str : list){
                    System.out.println(str);
                }
            }
        }

    }
}