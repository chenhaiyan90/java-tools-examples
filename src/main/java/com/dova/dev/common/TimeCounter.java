package com.dova.dev.common;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.security.SecureRandom;
import java.util.Map;

/**
 * Created by liuzhendong on 16/7/1.
 */
public class TimeCounter {

    public  Map<String,Cost> costMap = Maps.newConcurrentMap();
    public static final TimeCounter instance = new TimeCounter();
    public  class Cost{
        long last;
        long duration;
        public void start(){
            last = System.currentTimeMillis();
            duration = 0;
        }
        public long get(){
            return duration;
        }
        public synchronized void  add(){
            long now = System.currentTimeMillis();
            duration += now - last;
            last = now;
        }
    }

    public void startOrAdd(String key){
        Cost old = costMap.get(key);
        if(old != null){
            old.add();
        }
        synchronized (key){
            old = costMap.get(key);
            if(old == null){
                old = new Cost();
                old.start();
                costMap.put(key,old);
            }else {
                old.add();
            }
        }
    }

    public  long end(String key){
        Cost old = costMap.get(key);
        if(old == null){
            return 0;
        }else {
            old.add();
            costMap.remove(key);
            return old.get();
        }
    }
    public  void  endAndPrint(String key){
        long duration = end(key);
        System.out.println(String.format("key:%s cost:%dms",key,duration));
    }



    public static void sStartOrAdd(String key){
        instance.startOrAdd(key);
    }

    public  static long sEnd(String key){
      return instance.end(key);
    }
    public static void  sEndAndPrint(String key){
        long duration = instance.end(key);
        System.out.println(String.format("key:%s cost:%dms",key,duration));
    }
}
