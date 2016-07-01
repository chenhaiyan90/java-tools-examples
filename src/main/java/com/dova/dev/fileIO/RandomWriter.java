package com.dova.dev.fileIO;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.RandomAccessFile;
import java.util.Random;

/**
 * Created by liuzhendong on 16/6/24.
 */
public class RandomWriter{

    Random random = new Random();

    public RandomAccessFile  getTestRandomAccessFile() throws Exception{
        File file = File.createTempFile("random_"+random.nextInt()+"_",System.currentTimeMillis()+".txt",new File("/tmp/"));
        return  new RandomAccessFile(file,"rw");
    }


    public void write(RandomAccessFile randomFile,int num)throws Exception{
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        sb.append("[random_");
        for(int i = 0; i < 10;i++){
            sb.append(System.currentTimeMillis()+"_");
        }
        sb.append("_random]\n");
        String line = sb.toString();
        int len = line.length();
        System.out.println(String.format("lineSize:%d", line.length()));
        long start = System.currentTimeMillis();
        for(int i = 0; i< num;i++){
            randomFile.seek(random.nextInt(num) * len);
            randomFile.write(line.getBytes("utf-8"));
            /*
            if(randomFile.read() != -1){
                System.out.println("already write");
            }else {

            }*/
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("lines:%d cost:%dms",num,end-start));
    }
    @Test
    public void benchWrite()throws Exception{
        RandomAccessFile randomFile = getTestRandomAccessFile();
        randomFile.writeChars("start");
        write(randomFile,100*1000);
        randomFile.close();
    }

    @Test
    public void SingleThreadTest()throws Exception{
        long start = System.currentTimeMillis();
        RandomAccessFile randomFile = getTestRandomAccessFile();
        randomFile.writeChars("start");
        for (int i = 0; i < 100 ;i++){
            write(randomFile,100*1000);
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("cost:%dms",end-start));
    }


    @Test
    public void multiThreadTest()throws Exception{
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try{
                    benchWrite();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        RandomAccessFile randomFile = getTestRandomAccessFile();
        randomFile.writeChars("start");
        Runnable oneFileRun = new Runnable() {
            @Override
            public void run() {
                try{
                    write(randomFile,100*1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        Thread[] ts = new Thread[100];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(oneFileRun);
        }
        long start = System.currentTimeMillis();

        for (int i = 0; i < ts.length; i++) {
            ts[i].start();
        }
        for (int i = 0; i < ts.length; i++) {
            ts[i].join();
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("cost:%dms",end-start));


    }
}
