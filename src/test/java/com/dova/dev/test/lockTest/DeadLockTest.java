package com.dova.dev.test.lockTest;

import org.junit.Test;

import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by liuzhendong on 16/9/4.
 */
public class DeadLockTest {

    public ReentrantLock lockA = new ReentrantLock(true);
    public ReentrantLock lockB = new ReentrantLock(true);

    public void doA()throws Exception{
        lockA.lock();
        Thread.sleep(2000);
        lockB.lock();
        Thread.sleep(2000);
        lockA.unlock();
        lockB.unlock();
    }
    public void doB()throws Exception{
        lockB.lock();
        Thread.sleep(2000);
        lockA.lock();
        Thread.sleep(2000);
        lockB.unlock();
        lockA.unlock();
    }

    @Test
    public void testDeadLock()throws Exception{
        Thread a = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doA();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        Thread b = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    doB();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        a.start();
        b.start();
        a.join();
        b.join();
    }
}
