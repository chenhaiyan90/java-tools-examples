package com.dova.dev.netIO;

import java.net.Socket;

/**
 * Created by liuzhendong on 16/9/12.
 */
public class ClientSocketExample {

    public static void main(String[] args)throws Exception{
        int i =0;
        while (true){
            try {
                Socket socket = new Socket("182.92.183.219",8708);
                System.out.println(++i);
            }catch (Exception e){
                e.printStackTrace();
                try {
                    Thread.sleep(1000);
                }catch (Exception ee){
                    ee.printStackTrace();
                }
            }
        }
    }
}
