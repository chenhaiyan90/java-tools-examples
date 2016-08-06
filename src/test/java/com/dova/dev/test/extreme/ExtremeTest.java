package com.dova.dev.test.extreme;

import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Random;
import java.util.concurrent.locks.LockSupport;

/**
 * Created by liuzhendong on 16/8/6.
 */
public class ExtremeTest {

    Random random = new Random();

    @Test
    public void testMaxFileOpens()throws Exception{
        int num = 10000;
        //TODO 修改哪些参数 可以改变max files
        RandomAccessFile[] files = new RandomAccessFile[num];
        for (int i = 0; i < num; i++) {
            files[i] = new RandomAccessFile("/tmp/test." + i,"rw");
        }
    }

    @Test
    public void testMaxStacks()throws Exception{
        int num = 10000;
        byte[][] bytes = new byte[num][]; //放在堆中还是栈中
        for (int i = 0; i < num; i++) {
            System.out.println(i);
            bytes[i] = new byte[1024*1024];
        }
    }

    int a = 0;
    @Test
    public void testMaxStacks2() throws Exception{
        //TODO 是什么在影响stack over flow
        System.out.println(a++);
        testMaxStacks2();
    }



    @Test
    public void testMaxThreads() throws Exception{
        //JVM中有自己的线程栈大小,超过这个就会报OutOfMemoryError
        int num = 10000;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                LockSupport.park(this);
            }
        };
        Thread[] ts = new Thread[num];
        for (int i = 0; i < num; i++) {
            ts[i] = new Thread(runnable);
            ts[i].start();
            System.out.println(i);
        }
    }


    @Test
    public void testWriteDistance()throws Exception{
        RandomAccessFile access = new RandomAccessFile("/tmp/test","rw");
        int num = 1000 * 1000;
        long maxLen = 1024 * 1024 * 1024;
        int step = 32;
        int firstPos = 0;
        int currPos = 0;
        byte[] buff = new byte[128];
        boolean forward = true;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            access.seek(currPos);
            access.write(buff);
            if(forward){
                currPos += step;
                if(currPos > maxLen){
                    forward = false;
                }
            }else {
                currPos -= step;
                if(currPos < 0){
                    currPos = 0;
                    forward = true;
                }
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("num:%d cost:%d ms", num, end-start));
    }


    @Test
    public void testWriteTwoFiles()throws Exception{
        RandomAccessFile access1 = new RandomAccessFile("/tmp/test.1","rw");
        RandomAccessFile access2 = new RandomAccessFile("/tmp/test.2","rw");
        int num = 1000 * 1000;
        byte[] buff = new byte[128];
        random.nextBytes(buff);
        boolean isFirst = true;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            if(isFirst){
                access1.write(buff);
                isFirst = false;
            }else {
                access2.write(buff);
                isFirst = true;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("num:%d cost:%d ms", num, end-start));
    }



    @Test
    public void testTwoThreadWriteTwoFiles()throws Exception{
        final RandomAccessFile access1 = new RandomAccessFile("/tmp/test.1","rw");
        final RandomAccessFile access2 = new RandomAccessFile("/tmp/test.2","rw");
        final int num = 500 * 1000;
        final byte[] buff = new byte[128];
        random.nextBytes(buff);
        Runnable taska = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < num; i++) {
                        access1.write(buff);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };

        Runnable taskb = new Runnable() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < num; i++) {
                        access2.write(buff);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };

        Thread[] ts = new Thread[2];
        ts[0] = new Thread(taska);
        ts[1] = new Thread(taskb);

        long start = System.currentTimeMillis();
        for (int i = 0; i < ts.length; i++) {
            ts[i].start();
        }
        for (int i = 0; i < ts.length; i++) {
            ts[i].join();
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("num:%d cost:%d ms", num, end-start));
    }


}
