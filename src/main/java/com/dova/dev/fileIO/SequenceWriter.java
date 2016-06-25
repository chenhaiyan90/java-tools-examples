package com.dova.dev.fileIO;

import org.junit.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.security.SecureRandom;

/**
 * Created by liuzhendong on 16/6/24.
 */
public class SequenceWriter {


    public BufferedWriter getTestBufferedWriter()throws Exception{
        File file = File.createTempFile("sequence_",System.currentTimeMillis()+".txt",new File("/tmp/"));
        return  new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true)));
    }

    @Test
    public void benchWrite()throws Exception{
        BufferedWriter bufferedWriter = getTestBufferedWriter();
        StringBuilder sb = new StringBuilder();
        sb.append("[test_");
        for(int i = 0; i < 10;i++){
            sb.append(System.currentTimeMillis()+"_");
        }
        sb.append("test]\n");
        String line = sb.toString();
        System.out.println(String.format("lineSize:%d", line.length()));
        int num = 1000000;
        long start = System.currentTimeMillis();
        for(int i = 0; i< num;i++){
            bufferedWriter.write(line);
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("lines:%d cost:%dms",num,end-start));
        bufferedWriter.close();
    }
}
