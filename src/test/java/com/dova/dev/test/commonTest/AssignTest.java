package com.dova.dev.test.commonTest;

import org.junit.Test;

import java.util.Random;

/**
 * Created by liuzhendong on 16/6/28.
 */
public class AssignTest {

    Random random = new Random();

    @Test
    public void testCompare(){
        int num = 10000;
        int[] a = new int[num];
        for(int i = 0;i < num; i++){
            a[i] = random.nextInt(num*4);
        }
        long start = System.currentTimeMillis();
        for (int i=0;i< num;i++){
            int tmp = a[random.nextInt(num-1)];
            /*
            for (int aa:a){
                if(aa > tmp) tmp = aa;
            }*/
            for (int j=0; j < num;j++){
                if(a[j] > tmp);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("num:%d cost:%dms",num, end-start));
    }

    @Test
    public void testAssign(){
        int num = 10000;
        int[] a = new int[num];
        for(int i = 0;i < num; i++){
            a[i] = random.nextInt(num*4);
        }
        long start = System.currentTimeMillis();
        for (int i=0;i< num;i++){
            for (int j=0; j < num;j++){
                a[j] = random.nextInt(num*4);
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("num:%d cost:%dms",num, end-start));
    }
}
