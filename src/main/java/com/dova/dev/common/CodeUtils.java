package com.dova.dev.common;

import com.google.common.base.Strings;

import java.util.Random;

/**
 * Created by liuzhendong on 16/5/3.
 */
public class CodeUtils {
    private static Random random = new Random();
    private static char[] bases = {
        '0','1','2','3','4','5','6','7','8','9',
        'a' , 'b' , 'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
        'i' , 'j' , 'k' , 'l' , 'm' , 'n' , 'o' , 'p' ,
        'q' , 'r' , 's' , 't' , 'u' , 'v' , 'w' , 'x' ,
        'y' , 'z'
    };

    //32位对应的黄金分割数, 2^32 * 0.6180339887499
    public final static  long GOLD_NUMBER = 2654435761l;
    public final static  long RANDOM_SEED = random.nextInt(Integer.MAX_VALUE);


    public static  String  genCodeWithPhone(String phone,boolean conflict){
        long num = Long.valueOf(phone);
        num = hashWithGoldNumber(num);
        if(conflict){
            num = num + random.nextInt(100);
        }
        return  String.valueOf(getChars(num,6,5)).toUpperCase();
    }

    public static String genCodeWithId(int id, boolean conflict){
        long hash = hashWithGoldNumber(id);
        if(conflict){
            hash = hash + random.nextInt(100);
        }
        return  String.valueOf(getChars(hash,6,5)).toUpperCase();
    }

    private static char[]  getChars(long num,int len, int unit){
        char[] chars = new char[len];
        int tmp = (1 << unit) - 1;
        for (int i = 0; i < chars.length; i++) {
            int index = (char)(num >> (i * unit) & tmp);
            chars[i] = bases[index];

        }
        return chars;
    }


    public static long hashWithGoldNumber(long origin){
        //如果输入在某个范围内,哈希到同样的范围内,则冲突为0;
        //如果输入是随机数, 哈希到某个较小的范围,效果与hashWithBitOp差不多
        return  origin * GOLD_NUMBER;
    }

    public static long hashWithBitOp(long origin){
        //从hashmap中拷贝过来的
        long h = origin ^ RANDOM_SEED ;
        h ^= (h >>> 20) ^ (h >>> 12);
        h = h ^ (h >>> 7) ^ (h >>> 4);
        return  h;
    }

    public static String newCaptcha() {
        int number = random.nextInt(9999);
        return Strings.padStart(String.valueOf(number), 4, '0');
    }
}
