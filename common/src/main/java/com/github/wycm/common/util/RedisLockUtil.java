package com.github.wycm.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

@Slf4j
@Component
public class RedisLockUtil {

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "PX";
    private static final Long RELEASE_SUCCESS = 1L;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 尝试获取分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间 单位ms
     * @return 是否获取成功
     */
    public boolean lock(String lockKey, String requestId, int expireTime) {
        if (jedisPool.isClosed()){
            log.warn("jedisPool closed");
            return false;
        }
        Jedis jedis = jedisPool.getResource();
        String result = null;
        try{
            result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        } finally {
            jedis.close();
        }
        if (LOCK_SUCCESS.equals(result)) {
            return true;
        }
        return false;

    }
    /**
     * 释放分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean unlock(String lockKey, String requestId) {
        Jedis jedis = jedisPool.getResource();
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Object result = null;
        try {
            result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        } finally {
            jedis.close();
        }
        if (RELEASE_SUCCESS.equals(result)) {
            return true;
        }
        return false;
    }

}