package cn.pomelo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchDemo {
   private static final int PLAYER_AMOUNT = 5;
   public CountDownLatchDemo() {
       // TODO Auto-generated constructor stub
    }
   /**
    * @param args
    */
   public static void main(String[] args) {
       // TODO Auto-generated method stub
       //对于每位运动员，CountDownLatch减1后即结束比赛
       CountDownLatch begin = new CountDownLatch(1);
       //对于整个比赛，所有运动员结束后才算结束
       CountDownLatch end = new CountDownLatch(PLAYER_AMOUNT);
       Player[] plays = new Player[PLAYER_AMOUNT];

       for(int i=0;i<PLAYER_AMOUNT;i++)
           plays[i] = new Player(i+1,begin,end);

       //设置特定的线程池，大小为5
       ExecutorService exe = Executors.newFixedThreadPool(PLAYER_AMOUNT);
       for(Player p:plays)
           exe.execute(p);

       System.out.println("Race begins!");
       begin.countDown();//分配线程
       try{
           end.await();           	 //等待end状态变为0，即为比赛结束
       }catch (InterruptedException e) {
           e.printStackTrace();
       }finally{
           System.out.println("Race ends!");
       }
       exe.shutdown();
   }
}