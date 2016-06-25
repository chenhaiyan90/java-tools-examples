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


    public RandomAccessFile  getTestRandomAccessFile() throws Exception{
        File file = File.createTempFile("random_",System.currentTimeMillis()+".txt",new File("/tmp/"));
        return  new RandomAccessFile(file,"rw");
    }


    @Test
    public void benchWrite()throws Exception{
        RandomAccessFile randomFile = getTestRandomAccessFile();
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
        int num = 1000000;
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
        randomFile.close();
    }
}
