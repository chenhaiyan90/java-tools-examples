package com.dova.dev.test.lockTest;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RunnableFuture;

/**
 * Created by liuzhendong on 16/9/6.
 */
public class DeadThreadTest {

    ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Test
    public void testDead()throws Exception{
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    while (true){
                        try {
                            //Thread.sleep(500);
                            System.out.println("I'm On:" + Thread.currentThread().isInterrupted());
                        }catch (Exception e){
                            e.printStackTrace();
                            System.out.println(Thread.currentThread().isInterrupted());
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        Thread t = new Thread(runnable,"hehe");
        t.start();
        t.interrupt();
        System.out.println(t.isInterrupted());
        new CountDownLatch(1).await();
    }
}
