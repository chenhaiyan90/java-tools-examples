package com.dova.dev.rocketmq;

/**
 * Created by liuzhendong on 16/6/26.
 */
import java.util.List;
import java.util.Random;
import java.util.concurrent.Exchanger;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.dova.dev.Config;

public class Consumer {

    public static void main(String[] args){
        DefaultMQPushConsumer consumer =
                new DefaultMQPushConsumer("PushConsumer");
        consumer.setNamesrvAddr(Config.RocketMQ_NAME_SERVER);
        Random random = new Random();
        try {
            //订阅PushTopic下Tag为push的消息
            consumer.subscribe("PushTopic", "push");
            //程序第一次启动从消息队列头取数据
            consumer.setConsumeFromWhere(
                    ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
            consumer.registerMessageListener(
                    new MessageListenerConcurrently() {
                        public ConsumeConcurrentlyStatus consumeMessage(
                                List<MessageExt> list,
                                ConsumeConcurrentlyContext Context) {
                            MessageExt msg = (MessageExt)list.get(0);
                            //System.out.println(msg.toString());
                            try{
                                Thread.sleep(random.nextInt(100));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                            System.out.println(String.format("ts:%d topic:%s tags:%s keys:%s queueId:%d body:%s",
                                    Thread.currentThread().getId(),
                                    msg.getTopic(), msg.getTags(), msg.getKeys(), msg.getQueueId(),new String(msg.getBody())));
                            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                        }
                    }
            );
            consumer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}