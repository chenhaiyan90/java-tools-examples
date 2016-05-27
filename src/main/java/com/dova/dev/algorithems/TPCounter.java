package com.dova.dev.algorithems;

/**
 * Created by liuzhendong on 16/5/27.
 */
public class TPCounter {


    //前面1k以ms计,后面1k以s计算
    private int[] count = new int[2001];

    private long allCount = 0;


    public void  flow(long cost){
        allCount++;
        count[getIndex(cost)]++;
    }
    private int getIndex(long cost){
        if(cost/1000 == 0){
            return (int)cost;
        }
        int seconds = (int)(cost/1000);
        if(seconds > 1000){
            return count.length - 1;
        }
        return 1000 + seconds - 1;
    }


    //获取Tp值
    public int getTPValue(float ratio){
        if(ratio <= 0 || ratio >= 1){
            ratio = 0.99f;
        }
        long num = (long)(allCount * ratio);
        int tmp = 0;
        for(int i = count.length - 1; i>0; i--){
            tmp += count[i];
            if(tmp > num){
                return  i;
            }
        }
        return 0;
    }
}
