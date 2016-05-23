package com.dova.dev.test.lockTest;

import org.junit.Test;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by liuzhendong on 16/4/1.
 */
public class LocalLockTest{


    @Test
    public  void testLock()throws  Exception{
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

    @Test
    public void syncLock()throws Exception{
        final Object a = new Object();
        final Object b = new Object();
        Runnable runnable = new Runnable() {
            public void run() {
                synchronized (a){
                    try{
                        Thread.sleep(1000);
                        System.out.println("start:" + Thread.currentThread().getName() );
                        b.wait();
                        System.out.println("end:" + Thread.currentThread().getName() );
                    }catch (InterruptedException ex){

                    }
                }
            }
        };

        Thread t1 = new Thread(runnable);
        Thread t2 = new Thread(runnable);
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }

}
