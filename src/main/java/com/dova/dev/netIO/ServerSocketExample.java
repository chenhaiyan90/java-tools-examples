package com.dova.dev.netIO;

import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.concurrent.CountDownLatch;

/**
 * Created by liuzhendong on 16/9/12.
 */
public class ServerSocketExample {

    public ServerSocket serverSocket;

    public ServerSocketExample()throws Exception{
        serverSocket = new ServerSocket(8808,2000);
    }


    @Test
    public void testINetAddress()throws Exception{
        InetAddress address1 = Inet4Address.getByName("www.baidu.com");
        InetAddress address2 = Inet4Address.getByName("182.92.183.219");
        InetAddress address3 = Inet4Address.getLocalHost();
        InetAddress address4 = Inet4Address.getLoopbackAddress();
        System.out.println(address1.toString());
        System.out.println(address2.toString());
        System.out.println(address3.toString());
        System.out.println(address4.toString());
        System.out.println(address2.getHostName());
        System.out.println(address2.toString());

    }
    public static void main(String[] args)throws Exception{
        ServerSocketExample example = new ServerSocketExample();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        countDownLatch.await();
    }
}
