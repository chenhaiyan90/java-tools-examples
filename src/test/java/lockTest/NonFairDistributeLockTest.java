package lockTest;

import com.dova.dev.distribute.NonFairDistributeLock;

/**
 * Created by liuzhendong on 16/4/1.
 */
public class NonFairDistributeLockTest {
    //ReentrantLock lock = new ReentrantLock();
   // NonFairDistributeLock lock = new NonFairDistributeLock("XXXXXXXXXXXXXXX");
    class MyTask implements Runnable{
        NonFairDistributeLock lock;
        public  MyTask(NonFairDistributeLock lock){
            this.lock = lock;
        }
        public void run(){
            System.out.println(Thread.currentThread().getName() + "\tstart");
            lock.lock();
            try{
                System.out.println(Thread.currentThread().getName() + "\tlock start");
                Thread.sleep(1000);
                if((int)(Math.random() * 5) == 1){
                    throw  new Exception("抛出异常");
                };
                System.out.println(Thread.currentThread().getName() + "\tlock end");
            }catch (Exception e){
                System.out.println(Thread.currentThread().getName() + "\t" + e.getMessage());
            }finally {
                lock.unlock();
            }

        }
    };
    public  void testLock()throws  Exception{

        Thread[] ts = new Thread[10];
        for (int i =0; i < ts.length; i++){
            ts[i] = new Thread(new MyTask(new NonFairDistributeLock("XXXXXXXXXXXXXX")));
        }
        for (int i =0; i < ts.length; i++){
            ts[i].start();
        }
        for (int i =0; i < ts.length; i++){
            ts[i].join();
        }
    }
    public static void main(String[] args)throws  Exception{
        NonFairDistributeLockTest test = new NonFairDistributeLockTest();
        test.testLock();
    }

}
