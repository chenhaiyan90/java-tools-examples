package com.dova.dev.test.lockTest;

import org.junit.Assert;
import org.junit.Test;
import org.omg.PortableServer.THREAD_POLICY_ID;

import javax.jws.soap.SOAPBinding;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by liuzhendong on 16/5/21.
 */
public class ParkTest {


    public void parkMutliTimes()throws Exception{
        Callable<Boolean> callable =  () -> {
            System.out.println("start call ");
            Thread.sleep(1000);
            for(int i=0;i<10;i++){
                System.out.println("start park " + i);
                LockSupport.park(this);
                System.out.println("end park " + i);
            }
            return  Thread.currentThread().isInterrupted();
        };
        FutureTask task = new FutureTask(callable);
        Thread t =  new Thread(task);
        t.start();
        for(int i=0;i<10;i++){
            System.out.println("start unpark " + i);
            LockSupport.unpark(t);
            System.out.println("end unpark " + i);
        }
        t.join();
        Thread.sleep(1000);
        t.interrupt();
    }


    @Test
    public void parkAndInterupt()throws Exception{
        Callable<Boolean> callable =  () -> {
            System.out.println("start park " + Thread.currentThread().getName());
            for(;;){
                if(Thread.currentThread().isInterrupted()){
                    break;
                }
            }
            LockSupport.park(this);
            System.out.println(String.format("end park %s interrupted:%b",Thread.currentThread().getName(), Thread.currentThread().isInterrupted()));
            return  Thread.currentThread().isInterrupted();
        };
        FutureTask task = new FutureTask(callable);
        Thread t =  new Thread(task);
        t.start();
        t.interrupt();
        Assert.assertTrue((Boolean)task.get());
    }


    @Test
    public void testInterrupt()throws Exception{
        Callable<Boolean> callable =  () -> {
            System.out.println("start " + Thread.currentThread().getName());
            for(;;){
                if(Thread.currentThread().isInterrupted()){
                    break;
                }
            }
            System.out.println(String.format("end %s interrupted:%b",Thread.currentThread().getName(), Thread.currentThread().isInterrupted()));
            return  Thread.currentThread().isInterrupted();
        };
        FutureTask task = new FutureTask(callable);
        Thread t =  new Thread(task);
        t.start();
        t.interrupt();
        Assert.assertTrue((Boolean)task.get());
    }
}
