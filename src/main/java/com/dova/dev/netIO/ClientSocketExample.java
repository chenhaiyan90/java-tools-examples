package com.dova.dev.netIO;

import org.junit.Test;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by liuzhendong on 16/9/12.
 */
public class ClientSocketExample {

    @Test
    public void testScribble()throws Exception{
        Socket socket = new Socket("182.92.183.219",80);
        System.out.println(socket.getClass());
        System.out.println(socket.getLocalPort());
        int sendNum = 0;
        while (true){
            try {
                if(sendNum > 0)break;
                System.out.println("first_s=======");
                System.out.println(!socket.isClosed() && socket.isConnected());
                socket.getOutputStream().write("hello12345".getBytes());
                System.out.println(++sendNum);
                if(sendNum >= Integer.MAX_VALUE){
                    break;
                }
                System.out.println("first_e=======");
            }catch (Exception e){
                e.printStackTrace();
                break;
            }
        }
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }

    @Test
    public void testTillWrite()throws Exception{
        //注意 服务器不receive
        Socket socket = new Socket("182.92.183.219",8708);
        System.out.println(socket.getClass());
        int sendNum = 0;
        while (true){
            try {
                System.out.println("first_s=======");
                System.out.println(socket.getLocalPort());
                System.out.println(!socket.isClosed() && socket.isConnected());
                socket.getOutputStream().write("hello12345".getBytes());
                System.out.println(++sendNum);
                if(sendNum >= 100000){
                    break;
                }
                System.out.println("first_e=======");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        CountDownLatch latch = new CountDownLatch(1);
        latch.await();
    }

    @Test
    public void testTillConnect()throws Exception{
        //注意, 服务器不receive
        List<Socket> sockets = new ArrayList<Socket>(1024);
        int n =0;
        while (true){
            try {
                Socket socket = new Socket("182.92.183.219",8708);
                sockets.add(socket);
                System.out.println("=======================================");
                System.out.println(socket.getInetAddress());
                System.out.println(socket.getPort());
                System.out.println(socket.getLocalAddress());
                System.out.println(socket.getLocalPort());
                System.out.println(++n);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args)throws Exception{
        List<Socket> sockets = new ArrayList<Socket>(1024);
        int n =0;
        while (true){
            try {
                Socket socket = new Socket("182.92.183.219",8708);
                sockets.add(socket);
                System.out.println("=======================================");
                System.out.println(socket.getInetAddress());
                System.out.println(socket.getPort());
                System.out.println(socket.getLocalAddress());
                System.out.println(socket.getLocalPort());
                System.out.println(++n);
                if(n> 300){
                    break;
                }
            }catch (Exception e){
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                }catch (Exception ee){
                    ee.printStackTrace();
                }
            }
        }
        System.out.println("=======================================");
        Socket first = sockets.get(0);
        Socket last = sockets.get(sockets.size() - 1);
        int sendNum = 0;
        while (true){
            try {
                System.out.println("first_s=======");
                System.out.println(first.getLocalPort());
                System.out.println(!first.isClosed() && first.isConnected());
                first.getOutputStream().write("hello".getBytes());
                System.out.println(++sendNum);
                System.out.println("first_e=======");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Test
    public void testConnect()throws Exception{
        Socket socket =new Socket("182.92.183.219",8018);
        System.out.println(socket.getLocalPort());
        int i =0;
        while ((i++) < 4)
        socket.getOutputStream().write(("hello world" + i+"\n").getBytes());
        new CountDownLatch(1).await();
    }
}
