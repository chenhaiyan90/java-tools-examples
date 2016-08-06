package com.dova.dev.test.fileTest;

import org.junit.Test;

import java.io.RandomAccessFile;

/**
 * Created by liuzhendong on 16/8/5.
 */
public class FileTest {


    @Test
    public void testExtremeFileOpenFiles()throws Exception{
        RandomAccessFile[] rws = new RandomAccessFile[1000000];
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            rws[i] = new RandomAccessFile("/tmp/tmp_file_" + i,"rw");
            System.out.println(i);
        }
    }
}
