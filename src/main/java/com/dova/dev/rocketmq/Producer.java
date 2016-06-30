package com.dova.dev.rocketmq;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.dova.dev.Config;

import java.util.Arrays;

/**
 * Created by liuzhendong on 16/6/26.
 */


public class Producer {

    public DefaultMQProducer createProducer(){
        DefaultMQProducer producer = new DefaultMQProducer("Producer");
        producer.setNamesrvAddr(Config.RocketMQ_NAME_SERVER);
        return producer;
    }
    public void start(DefaultMQProducer producer)throws MQClientException{
        producer.start();
    }
    public void stop(DefaultMQProducer producer)throws MQClientException{
        producer.shutdown();
    }

    public void testMultThread(int num,int each)throws Exception{
        final DefaultMQProducer producer = createProducer();
        start(producer);
        Runnable task = new Runnable() {
            @Override
            public void run() {
                for(int i = 0;i < each;i++){
                    Message msg = new Message("PushTopic",
                            "push",
                            (Thread.currentThread().getName() + "." + i).getBytes());
                    try {
                        SendResult result = producer.send(msg);

                        System.out.println("i:" + i + " id:" + result.getMsgId() +
                                " result:" + result.getSendStatus());
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }
        };
        Thread[] ts = new Thread[num];
        for (int i = 0; i < ts.length;i++){
            ts[i] = new Thread(task);
        }
        long start = System.currentTimeMillis();
        for (int i = 0; i < ts.length;i++){
            ts[i].start();
        }
        for (int i = 0; i < ts.length;i++){
            ts[i].join();
        }
        long end  =  System.currentTimeMillis();
        System.out.println(String.format("ts:%d each:%d cost:%dms",num,each,end-start));
        stop(producer);
    }


    public static void main(String[] args)throws Exception{
        Producer producer = new Producer();
        producer.testMultThread(3,330);
    }
}