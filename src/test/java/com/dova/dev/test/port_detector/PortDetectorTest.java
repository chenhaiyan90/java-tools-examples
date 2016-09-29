package com.dova.dev.test.port_detector;

import com.dova.dev.port_detector.IpCreator;
import com.dova.dev.port_detector.PortDetector;
import com.dova.dev.port_detector.ResultCollector;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by liuzhendong on 16/9/22.
 */
public class PortDetectorTest {


    @Test
    public void emitTest(){
        IpCreator ipCreator = new IpCreator("182.92.183.218","255.255.255.255");
        PortDetector portDetector = new PortDetector();
        long start = System.currentTimeMillis();
        long num = 0;
        while (true){
            String ip = ipCreator.emit();
            portDetector.accept(ip);
            //System.out.println(ip);
            System.out.println(portDetector.emit());
            num++;
            if(num > 10000){
                break;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("num:%d cost:%d ms",num, end - start));
    }

    @Test
    public void collectTest()throws IOException{
        IpCreator ipCreator = new IpCreator("182.92.183.218","255.255.255.255");
        PortDetector portDetector = new PortDetector();

        ResultCollector resultCollector=null;
        try {
            resultCollector = new ResultCollector("/export/dm/redisBomb/redis.port.dm");
        }catch (Exception e){
            e.printStackTrace();
        }
        long start = System.currentTimeMillis();
        long num = 0;
        while (true){
            String ip = ipCreator.emit();
            //System.out.println(ip);
            String res = portDetector.accept(ip).emit();
            resultCollector.accept(res+"\n");
            System.out.println(res);
            num++;
            if(num > 10000){
                break;
            }
        }
        long end = System.currentTimeMillis();
        System.out.println(String.format("num:%d cost:%d ms",num, end - start));
    }
}
