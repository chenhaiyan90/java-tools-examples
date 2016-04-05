package lockTest;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by liuzhendong on 16/4/1.
 */
public class LocalLockTest{
    ReentrantLock lock = new ReentrantLock();
    Runnable  task = new Runnable() {
        public void run(){
            System.out.println(Thread.currentThread().getName() + "\tstart");
            lock.lock();
            try{
                Thread.sleep(1000);
                if((int)(Math.random() * 5) == 1){
                    throw  new Exception("抛出异常,并没有释放锁");
                }
                lock.unlock();
                System.out.println(Thread.currentThread().getName() + "\tend");
            }catch (Exception e){
                System.out.println(Thread.currentThread().getName() + "\t" + e.getMessage());
            }

        }
    };
    public  void testLock()throws  Exception{

        Thread[] ts = new Thread[10];
        for (int i =0; i < ts.length; i++){
            ts[i] = new Thread(task);
        }
        for (int i =0; i < ts.length; i++){
            ts[i].start();
        }
        for (int i =0; i < ts.length; i++){
            ts[i].join();
        }
    }
    public static void main(String[] args)throws  Exception{
        LocalLockTest test = new LocalLockTest();
        test.testLock();
    }

}
