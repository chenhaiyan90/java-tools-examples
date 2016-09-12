package com.dova.dev.test.lockTest;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuzhendong on 16/9/7.
 */
public class ListTest {

    @Test
    public void concurrAddList()throws Exception{
        final List<String>  strs =  new ArrayList<>();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    strs.add(Thread.currentThread().getName() + " " + i);
                }
            }
        };
        Thread[] ts = new Thread[5];
        for (int i = 0; i < ts.length; i++) {
            ts[i] = new Thread(runnable);
        }
        for (int i = 0; i < ts.length; i++) {
            ts[i].start();
        }
        for (int i = 0; i < ts.length; i++) {
            ts[i].join();
        }
        System.out.println(strs);
        System.out.println(strs.size());
    }
}
