package com.dova.dev.test.extreme;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
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
        RandomAccessFile access2 = new RandomAccessFile("/export/data/test.2","rw");
        int num = 1000 * 1000;
        byte[] buff = new byte[128];
        boolean isFirst = true;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            random.nextBytes(buff);
            if(isFirst){
                access1.write(buff);
                //isFirst = false;
            }else {
                access2.write(buff);
                //isFirst = true;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("num:%d cost:%d ms", num, end-start));
    }



    @Test
    public void batchTestWriteRandomFiles() throws Exception{
        for (int i = 1; i <= 10; i++) {
            testWriteRandomFiles(i);
        }
    }
    public void testWriteRandomFiles(int fileNum)throws Exception{
        int num = 10 * 1000 * 1000;

        int randomNum = fileNum;
        RandomAccessFile[] accessFiles = new RandomAccessFile[randomNum];
        for (int i = 0; i < randomNum; i++) {
            accessFiles[i] = new RandomAccessFile("/tmp/test." + i, "rw");
            accessFiles[i].setLength((num/randomNum) * 64);
        }
        byte[] buff = new byte[64];
        boolean isFirst = true;
        long start = System.currentTimeMillis();
        for (int i = 0; i < num; i++) {
            random.nextBytes(buff);
            accessFiles[random.nextInt(randomNum)].write(buff);
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("num:%d randomNum:%d cost:%d ms", num, randomNum, end-start));
        for (int i = 0; i < randomNum; i++) {
            accessFiles[i].close();
        }
    }




    class WriteTask implements Runnable{
        RandomAccessFile access;
        int writeNum;
        final byte[] buff = new byte[64];
        public WriteTask(RandomAccessFile access, int writeNum)throws IOException{
            this.access = access;
            this.access.setLength(writeNum * 64);
            this.writeNum = writeNum;
            random.nextBytes(buff);
        }

        @Override
        public void run() {
            try {
                for (int i = 0; i < writeNum; i++) {
                    //random.nextBytes(buff);
                    access.write(buff);
                }
                this.access.close();
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }

    public void testMultiThreadsWriteRandomFiles(int tsNum)throws Exception{
        final int totalNum = 10 * 1000 * 1000;

        Thread[] ts = new Thread[tsNum];
        for (int i = 0; i < tsNum; i++) {
            ts[i] = new Thread(new WriteTask(new RandomAccessFile("/tmp/test." + i,"rw"),totalNum/tsNum));
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < ts.length; i++) {
            ts[i].start();
        }
        for (int i = 0; i < ts.length; i++) {
            ts[i].join();
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("num:%d tsNum:%d cost:%d ms", totalNum, tsNum, end-start));
    }

    @Test
    public void batchTestMultiThreadsWriteRandomFiles()throws Exception{
        for (int i = 1; i <= 100; i++) {
            testMultiThreadsWriteRandomFiles(i);
        }
    }

}
