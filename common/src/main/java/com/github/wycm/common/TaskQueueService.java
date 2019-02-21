package com.github.wycm.common;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.function.Consumer;

/**
 * Created by wycm on 2018/10/24.
 */
@Service
@Slf4j
public class TaskQueueService {

    @Autowired
    private JedisPool jedisPool;

    /**
     *
     * @param queueName
     * @param maxLength
     */
    public void sendTask(String queueName, CrawlerMessage message, int maxLength){
        sendTask(queueName, JSON.toJSONString(message), maxLength);
    }

    public void sendTask(String queueName, CrawlerMessage message, int maxLength, Consumer<String> sendSuccConsumer){
        sendTask(queueName, JSON.toJSONString(message), maxLength, sendSuccConsumer);
    }

    public void sendTask(String queueName, String message, int maxLength, Consumer<String> sendSuccConsumer){
        Jedis jedis = jedisPool.getResource();
        try {
            if (queueSize(queueName, jedis) < maxLength){
                jedis.lpush(queueName, message);
                sendSuccConsumer.accept(message);
            } else {
                log.debug("queue is full, queueName:{}", queueName);
            }
        } finally {
            jedis.close();
        }
    }

    public void sendTask(String queueName, String message, int maxLength){
        Jedis jedis = jedisPool.getResource();
        try {
            if (queueSize(queueName, jedis) < maxLength){
                jedis.lpush(queueName, message);
            } else {
                log.debug("queue is full, queueName:{}", queueName);
            }
        } finally {
            jedis.close();
        }
    }

    private Long queueSize(String queueName, Jedis jedis){
        return jedis.llen(queueName);
    }

    public Long queueSize(String queueName){
        Long s = 0l;
        Jedis jedis = jedisPool.getResource();
        try {
            s = queueSize(queueName, jedis);
        } finally {
            jedis.close();
        }
        return s;
    }

    public CrawlerMessage receiveTask(String queueName) throws InterruptedException {
        String s = null;
        while (true){
            Jedis jedis = jedisPool.getResource();
            try {
                s = jedis.rpop(queueName);
            } finally {
                jedis.close();
            }
            if (StringUtils.isNotBlank(s)){
                break;
            }
            Thread.sleep(1000);
        }
        CrawlerMessage crawlerMessage = JSON.parseObject(s, CrawlerMessage.class);
        return crawlerMessage;
    }

    public Proxy receiveProxyTask(String queueName) throws InterruptedException {
        String s = null;
        while (true){
            Jedis jedis = jedisPool.getResource();
            try {
                s = jedis.rpop(queueName);
            } finally {
                jedis.close();
            }
            if (StringUtils.isNotBlank(s)){
                break;
            }
            Thread.sleep(1000);
        }
        Proxy proxy = JSON.parseObject(s, Proxy.class);
        return proxy;
    }

    public CrawlerMessage receiveTaskNotBlocking(String queueName){
        Jedis jedis = jedisPool.getResource();
        String s = null;
        try {
            s = jedis.rpop(queueName);
        } finally {
            jedis.close();
        }
        if (s == null){
            return null;
        }
        CrawlerMessage crawlerMessage = JSON.parseObject(s, CrawlerMessage.class);
        return crawlerMessage;
    }
}
