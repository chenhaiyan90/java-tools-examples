package com.dova.dev.test.concurrTest;

import org.junit.Test;

/**
 * Created by liuzhendong on 16/9/23.
 */
public class InterruptTest {

    @Test
    public void testInterrupt()throws Exception{
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()){
                    try {
                        System.out.println("run...");
                    }catch (Exception ignore){

                    }
                }
                System.out.println(Thread.currentThread().toString() +" is interrupted and will go die");
            }
        };
        Thread t = new Thread(runnable);
        t.start();
        Thread.sleep(1000);
        t.interrupt();
    }
}
