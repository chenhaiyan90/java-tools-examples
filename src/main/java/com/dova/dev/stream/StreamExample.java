package com.dova.dev.stream;

import java.util.Arrays;

/**
 * Created by liuzhendong on 16/6/30.
 */
public class StreamExample {

    public int sum(int[] a){
        return Arrays.stream(a).sum();
    }
}
