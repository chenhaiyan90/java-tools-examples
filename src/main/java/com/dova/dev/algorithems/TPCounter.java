package com.dova.dev.algorithems;

/**
 * Created by liuzhendong on 16/5/27.
 * 亿万级数据的TP999计算
 */
public class TPCounter {


    //前面1k以ms计,后面1k以10ms为计算,精度可以自定义
    private int[] count = new int[2001];

    private long allCount = 0;


    public void  flow(long cost){
        allCount++;
        count[getIndex(cost)]++;
    }
    private int getIndex(long cost){
        if(cost < 1000){
            return (int)cost;
        }
        int units = (int)((cost - 1000)/10);
        if(units > 1000){
            return count.length - 1;
        }
        return 1000 + units;
    }

    private int convert(int index){
        if(index < 1000){
            return index;
        }
        return  (index - 1000) * 10 + 1000;
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
                return  convert(i);
            }
        }
        return 0;
    }
}
