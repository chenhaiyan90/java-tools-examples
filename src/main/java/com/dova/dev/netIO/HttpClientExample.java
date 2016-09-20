package com.dova.dev.netIO;

import org.junit.Test;

import java.net.Socket;

/**
 * Created by liuzhendong on 16/9/19.
 */
public class HttpClientExample{

    @Test
    public void testRequest()throws Exception{
        Socket socket = new Socket("101.200.237.221",80);
        System.out.println("socket port:" + socket.getLocalPort());
        StringBuilder sb = new StringBuilder();
        sb.append("GET /index.html HTTP/1.1\r\n");
        sb.append("Host: www.futureinst.com\r\n");
        sb.append("Connection: keep-alive\r\n");
        sb.append("Content-Length:100\r\n");
        sb.append("\r\n");
        socket.getOutputStream().write(sb.toString().getBytes());
        byte[] bytes = new byte[1024 * 16];
        int num = socket.getInputStream().read(bytes);
        String res = new String(bytes,0,num);
        System.out.println("res:"+res.length()+"\n"+res);
        Thread.sleep(3000);
        socket.close();
    }
}
