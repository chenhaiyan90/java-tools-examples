package com.dova.dev.test.port_detector;

import org.junit.Test;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * Created by liuzhendong on 16/9/24.
 */
public class RedisTest {

    @Test
    public void testSetRedis(){
        String host = "182.92.71.171";
        int port = 6379;
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(host, port), 200);
            socket.setSoTimeout(200);
            StringBuilder sb = new StringBuilder();
            sb.append("*3\r\n");
            sb.append("$3\r\n");
            sb.append("SET\r\n");
            sb.append("$6\r\n");
            sb.append("pubkey\r\n");
            sb.append("$4\r\n");
            sb.append("test\r\n");
            socket.getOutputStream().write(sb.toString().getBytes());
            byte[] input = new byte[1024];
            int num = socket.getInputStream().read(input);
            System.out.println(new String(input,0,num));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
