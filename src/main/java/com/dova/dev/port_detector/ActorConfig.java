package com.dova.dev.port_detector;

import java.util.concurrent.BlockingQueue;

/**
 * Created by liuzhendong on 16/9/23.
 */
public class ActorConfig {

    public String name;
    public int threadNum;
    public Actor actor;
    public BlockingQueue from;
    public BlockingQueue to;
    public Thread[] ts;

    public ActorConfig(){

    }

    public ActorConfig name(String name){
        this.name = name;
        return this;
    }
    public ActorConfig actor(Actor actor){
        this.actor = actor;
        return this;
    }
    public ActorConfig threadNum(int num){
        this.threadNum = num;
        return this;
    }
    public ActorConfig from(BlockingQueue from){
        this.from = from;
        return  this;
    }
    public ActorConfig to(BlockingQueue to){
        this.to = to;
        return this;
    }
}
