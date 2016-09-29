package com.dova.dev.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.net.Socket;

/**
 * Created by liuzhendong on 16/9/20.
 */
public class ConnTest {


    @Test
    public void testConnect()throws Exception{
        //随意给一个端口,去连接会有什么效果,结果是报read time out exception
        String ip = "172.21.0.10";
        int port = 6379;
        Socket socket = new Socket(ip,port);
        System.out.println(socket.getPort()+" is ok");
        Jedis jedis = new Jedis(ip, port);
        System.out.println("connect ok");
        System.out.println(jedis.get("pubkey"));
        System.out.println("query ok");
    }
}
