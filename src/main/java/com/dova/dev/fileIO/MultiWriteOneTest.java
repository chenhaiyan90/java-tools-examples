package com.dova.dev.fileIO;

import org.junit.Test;

import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by liuzhendong on 16/9/23.
 */
public class MultiWriteOneTest {


    @Test
    public void testMultiWriteOne()throws Exception{
        String fileName = "/tmp/multi2one.tmp";
        FileChannel fch = FileChannel.open(Paths.get(fileName), StandardOpenOption.CREATE,StandardOpenOption.WRITE);
        //FileChannel fch2 = FileChannel.open(Paths.get(fileName), StandardOpenOption.CREATE,StandardOpenOption.WRITE);
        class Task implements Runnable{
            FileChannel myfch;
            ByteBuffer byteBuffer = ByteBuffer.allocate(100);
            public Task(FileChannel fch){
                this.myfch = fch;
            }
            public void run(){
                int num = 0;
                while (num < 100){
                    try {
                        String msg = Thread.currentThread().getName()+":" +num+"\n";
                        System.out.println(msg);
                        byteBuffer.put(msg.getBytes());
                        byteBuffer.flip();
                        myfch.write(byteBuffer);
                        byteBuffer.clear();
                        num++;
                        Thread.sleep(100);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        Thread t1 = new Thread(new Task(fch));
        Thread t2 = new Thread(new Task(fch));
        t1.start();
        t2.start();
        t1.join();
        t2.join();
    }
}
