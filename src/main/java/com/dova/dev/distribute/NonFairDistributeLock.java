package com.dova.dev.distribute;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.UUID;

/**
 * Created by liuzhendong on 16/4/1.
 */
public class NonFairDistributeLock {

    final JedisPool pool = new JedisPool(generateJedisConfig(), "xxx.xxx.xxx.xxx");

    final  String JEDIS_LOCK_KEY;

    public  NonFairDistributeLock(final String jedisLockKey){
        JEDIS_LOCK_KEY = jedisLockKey;
    }

    public  JedisPoolConfig generateJedisConfig(){
        JedisPoolConfig jconfig = new JedisPoolConfig();
        jconfig.setMaxTotal(100);
        return  jconfig;
    }

    public String generateKeyForLock(){
        Jedis jedis = pool.getResource();
        String key = UUID.randomUUID().toString();
        while (jedis.exists(key)){
            key = UUID.randomUUID().toString();
        }
        jedis.lpush(key,key);
        jedis.close();
        return  key;
    }
    public void  lock(){
        Jedis jedis = pool.getResource();
        long len = jedis.llen(JEDIS_LOCK_KEY);
        if(len > 1){
            System.out.println("分布式锁异常");
        }
        //System.out.println("lock" + JEDIS_LOCK_KEY);
        while (jedis.blpop(100,JEDIS_LOCK_KEY) == null){
            //do nothing
        }
        jedis.close();
    }

    public  void  unlock(){
        //System.out.println("unlock" + JEDIS_LOCK_KEY);
        Jedis jedis = pool.getResource();
        long len = jedis.llen(JEDIS_LOCK_KEY);
        if(len != 0){
            System.out.println("分布式锁异常");
        }
        //System.out.println("unlock" + JEDIS_LOCK_KEY);
        jedis.lpush(JEDIS_LOCK_KEY,JEDIS_LOCK_KEY);
        jedis.close();
    }

}
