package com.github.wycm.common;

import com.alibaba.fastjson.JSON;
import com.github.wycm.common.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
@Slf4j
public class ProxyQueue {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private JedisPool jedisPool;

    /**
     * 从队列中获取一个代理
     * @param queueName
     * @return
     */
    public Proxy takeProxy(String queueName) throws InterruptedException {
        String s = null;
        Proxy currentProxy = null;
        while (s == null){
            Jedis jedis = jedisPool.getResource();
            try {
                s = jedis.rpop(queueName);
                jedis.expire(queueName, Constants.REDIS_TIMEOUT);
                if (s != null){
                    currentProxy = JSON.parseObject(s, Proxy.class);
                    if (currentProxy.getPort() == LocalIPService.DIRECT_CONNECT_PORT){
                        //direct connect
                        if (!currentProxy.getIp().equals(LocalIPService.getLocalIp())){
                            //not local, add to queue
                            addProxy(queueName, currentProxy);
                            s = null;
                        }
                    }
                }

            } finally {
                jedis.close();
            }
            Thread.sleep(100);
        }
        Jedis jedis = jedisPool.getResource();
        long serverTime = 0;
        try {
            serverTime = getRedisServerTime(jedis);
            String queueSetName = queueName + "-set";
            jedis.srem(queueSetName, currentProxy.getProxyStr());
        } finally {
            jedis.close();
        }
        if ((serverTime - currentProxy.getLastUseTime()) * 1000 < Constants.TIME_INTERVAL){
            long sleepTime = Constants.TIME_INTERVAL - ((serverTime - currentProxy.getLastUseTime()) * 1000);
            logger.info("proxy failed to reach the specified delay, queueName:{}, sleep:{}", queueName, sleepTime);
            Thread.sleep(sleepTime);
        } else {
            logger.info("proxy reaches the specified delay, queueName:{}, {}ms", queueName, Constants.TIME_INTERVAL);
        }
        return currentProxy;
    }

    public void addProxy(String queueName, Proxy proxy){
        Jedis jedis = jedisPool.getResource();
        try {
            String queueSetName = queueName + "-set";
            if (!jedis.sismember(queueSetName, proxy.getProxyStr())){
                jedis.sadd(queueSetName, proxy.getProxyStr());
            } else {
                log.info("proxy exist, {}", proxy.getProxyStr());
                return;
            }
            proxy.setLastUseTime(getRedisServerTime(jedis));
            jedis.lpush(queueName, JSON.toJSONString(proxy));
            jedis.expire(queueName, Constants.REDIS_TIMEOUT);
            jedis.expire(queueSetName, Constants.REDIS_TIMEOUT);
        } finally {
            jedis.close();
        }
    }

    /**
     * 获取server时间戳
     * @return
     */
    public long getRedisServerTime(){
        Jedis jedis = jedisPool.getResource();
        long time = 0l;
        try {
            time = getRedisServerTime(jedis);
        } finally {
            jedis.close();
        }
        return time;
    }
    public long getRedisServerTime(Jedis jedis){
        long time = Long.valueOf(jedis.eval("return redis.call('time')[1]").toString());
        return time;
    }
}
