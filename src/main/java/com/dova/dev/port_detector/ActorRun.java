package com.dova.dev.port_detector;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by liuzhendong on 16/9/22.
 *
 * actor的管理
 */
public class ActorRun {

    private BlockingQueue ip2port;
    private BlockingQueue port2res;

    private Actor ipCreator;
    private Actor portDetector;
    private Actor resCollector;
    private List<ActorConfig> actorConfigs;

    private int status = 0;

    public ActorRun(){
        try {
            init();
            status = 1;
            System.out.println("Init Succ");
            System.out.println(Conf.get(Conf.START_IP));
            System.out.println(Conf.get(Conf.END_IP));
            System.out.println(Conf.get(Conf.PORT_DETECTOR_TS_NUM));
            System.out.println(Conf.get(Conf.DATA_DIR));
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Init Failed");
            status = -1;
        }
    }

    private void init()throws IOException{
        ipCreator = new IpCreator(Conf.get(Conf.START_IP), Conf.get(Conf.END_IP));
        portDetector = new PortDetector();
        resCollector = new ResultCollector(Conf.get(Conf.DATA_DIR) + "redis.port.dm");
        ip2port = new ArrayBlockingQueue(16 * 1024);
        port2res = new ArrayBlockingQueue(1024);
        //初始化actor config
        actorConfigs = new ArrayList<>();
        actorConfigs.add(new ActorConfig().name("ipCreator").actor(ipCreator).threadNum(1).from(null).to(ip2port));
        actorConfigs.add(new ActorConfig().name("portDetector").actor(portDetector).threadNum(Integer.valueOf(Conf.get(Conf.PORT_DETECTOR_TS_NUM))).from(ip2port).to(port2res));
        actorConfigs.add(new ActorConfig().name("resCollector").actor(resCollector).threadNum(1).from(port2res).to(null));

    }
    public class Worker implements Runnable{
        private BlockingQueue from; //可以为null
        private BlockingQueue to; //可以为null
        private Actor actor;
        public Worker(BlockingQueue from, BlockingQueue to,Actor actor){
            this.from = from;
            this.to =  to;
            this.actor = actor;
        }
        public void run(){
            while (!Thread.currentThread().isInterrupted()){
                try {
                    Object fromObj = from == null ? null : from.take();
                    Object toObj = actor.accept(fromObj).emit();
                    if(to != null && toObj != null) to.put(toObj);
                }catch (InterruptedException ignore){
                    break;
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            System.out.println(Thread.currentThread().getName() + " is interrupted, go die");
        }
    }


    public void start(){
        if(status == -1){
            System.out.println("cannot start at status " + status);
            return;
        }
        try {
            for (ActorConfig config : actorConfigs) {
                Thread[] ts = new Thread[config.threadNum];
                config.ts = ts;
                for (int i = 0; i < ts.length; i++) {
                    if (0 == i) {
                        ts[i] = new Thread(new Worker(config.from, config.to, config.actor), config.name+"-" + i);
                    } else {
                        ts[i] = new Thread(new Worker(config.from, config.to, config.actor.clone()), config.name+"-" + i);
                    }
                }
                for (int i = 0; i < ts.length; i++) {
                    ts[i].start();
                }
            }

        }catch (Exception e){
            System.out.println("start failed:");
            e.printStackTrace();
            tryStop();
        }
    }

    private void tryStop(){
        if(status == -1){
            System.out.println("no need stop at status " + status);
            return;
        }
        for (ActorConfig config : actorConfigs) {
            if(config.ts == null || config.ts.length == 0){
                continue;
            }
            for (Thread t : config.ts){
                try {
                    if(t.isAlive() && !t.isInterrupted()) {
                        t.interrupt();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public void stop(){
        tryStop();
    }

    public static void main(String args[])throws Exception{
        ActorRun actorRun = new ActorRun();
        actorRun.start();
        Thread.sleep(100 * 1000);
        new CountDownLatch(1).await();
        actorRun.stop();
    }
}
