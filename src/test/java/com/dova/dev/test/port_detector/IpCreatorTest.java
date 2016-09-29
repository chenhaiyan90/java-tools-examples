package com.dova.dev.test.port_detector;

import com.dova.dev.port_detector.Conf;
import com.dova.dev.port_detector.IpCreator;
import org.junit.Test;

/**
 * Created by liuzhendong on 16/9/22.
 */
public class IpCreatorTest {


    @Test
    public void testConvert(){
        String ipStr = "182.92.183.219";
        long ip = IpCreator.parseIp(ipStr);
        System.out.println(ipStr);
        System.out.println(ip);
        System.out.println(IpCreator.convertToIp(ip));
        System.out.println((long)1 << 24);
    }

    @Test
    public void testEmit(){
        IpCreator ipCreator = new IpCreator("182.92.0.0","182.92.10.10");
        long start = System.currentTimeMillis();
        long num = 0;
        while (true){
            String ip = ipCreator.emit();
            num++;
            if(num % 1000 == 0){
                System.out.println(ip);
            }
            if(num > 100 * 1000 * 1000){
                break;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("num:%d cost:%dms", num, end - start));
    }
}
