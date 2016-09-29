package com.dova.dev.port_detector;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by liuzhendong on 16/9/22.
 * A类地址:1～126, 其中私有地址段:10.0.0.0～10.255.255.255
 * B类地址:128~191, 其中私有地址段:172.16.0.0～172.131.255.255
 * C类地址:192~233, 其中私有地址段:192.168.0.0～192.168.255.255
 */

public class IpCreator extends Actor{
    public long startIp;
    public long endIp;
    public AtomicLong currIp;

    public IpCreator(String start,String end){
        this.startIp = parseIp(start);
        this.currIp = new AtomicLong(this.startIp);
        this.endIp = parseIp(end);
        if(this.endIp < this.endIp){
            throw new IllegalArgumentException(String.format("start must lt or eq end,! %s <= %s",start,end));
        }
    }

    @Override
    public String emit(){
        while (true){
            long tmpIp = currIp.addAndGet(1);
            long first = (tmpIp>>>24) & 0xFF;
            long second = (tmpIp>>>16) & 0xFF;
            if(first == 10
                    || (first == 172 && (second >= 16 || second <= 131))
                    || (first == 192 && second == 168)
                    ){
                continue;
            }
            if(first == 255 || tmpIp >= endIp){
                try {
                    Thread.sleep(1000 * 1000);
                }catch (Exception e){
                    e.printStackTrace();
                }
                continue;
            }
            return convertToIp(tmpIp);
        }
    }

    @Override
    public IpCreator accept(Object from){
        return this;
    }


    public static String convertToIp(long ip){
        String byte1 = String.valueOf( (ip >>> 24) & 0xFF);
        String byte2 = String.valueOf( (ip >>> 16) & 0xFF);
        String byte3 = String.valueOf( (ip >>> 8) & 0xFF);
        String byte4 = String.valueOf( (ip) & 0xFF);

        return byte1 + "." + byte2 + "." + byte3 + "." + byte4;
    }
    public static long parseIp(String ip){
        String[] items = ip.trim().split("\\.");
        long a1 = Long.valueOf(items[0]);
        long a2 = Long.valueOf(items[1]);
        long a3 = Long.valueOf(items[2]);
        long a4 = Long.valueOf(items[3]);
        return (a1 << 24) + (a2 << 16) + (a3 << 8) + a4;
    }
}
