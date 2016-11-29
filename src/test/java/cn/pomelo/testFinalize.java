package cn.pomelo;

/**
 * finalize 只会被执行一次
 * 
 */
public class testFinalize {

    private static testFinalize demo = null;
    
    public static void main(String[] args) throws InterruptedException {
        demo = new testFinalize();
        demo = null;
        System.gc();
        Thread.sleep(500);
        if(demo != null){
            System.out.println("I am live" );
        }else{
            System.out.println("I am die" );
        }
        
        System.gc();
        if(demo != null){
            System.out.println("I am live" );
        }else{
            System.out.println("I am die" );
        }
    }
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        System.out.println("This execute finalize method");
        demo = this;
    }
}