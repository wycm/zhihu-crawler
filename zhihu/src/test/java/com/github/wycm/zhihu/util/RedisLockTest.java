package com.github.wycm.zhihu.util;

import com.github.wycm.common.util.RedisLockUtil;
import com.github.wycm.zhihu.ZhihuCrawlerApplication;
import com.github.wycm.zhihu.SimpleSpringJUnit4ClassRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.UUID;

@RunWith(SimpleSpringJUnit4ClassRunner.class)
@SpringBootTest(classes= ZhihuCrawlerApplication.class)
public class RedisLockTest {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private RedisLockUtil  redisLockUtil;



    @Test
    public void testLock(){
        Jedis jedis = jedisPool.getResource();
        String lockKey = "proxy";
        String id = UUID.randomUUID().toString();
        Boolean result = redisLockUtil.lock(lockKey, id, 5000);
        Assert.assertTrue(result);

        result = redisLockUtil.lock(lockKey, id, 5000);
        Assert.assertFalse(result);
        redisLockUtil.unlock(lockKey, id);
        Assert.assertTrue(redisLockUtil.lock(lockKey, id, 5000));
        jedis.close();
    }

    @Test
    public void testTime(){
        Jedis jedis = jedisPool.getResource();
        Object o = jedis.eval("return redis.call('time')[1]");
        System.out.println(o.toString());
        jedis.close();
    }

    @Test
    public void redisListTest(){
        Jedis jedis = jedisPool.getResource();
        jedis.del("1");
        jedis.lpush("1", "1");
        jedis.lpush("1", "2");
        jedis.lpush("1", "3");

        Assert.assertTrue(jedis.rpop("1").equals("1"));
        Assert.assertTrue(jedis.rpop("1").equals("2"));
        Assert.assertTrue(jedis.rpop("1").equals("3"));
        Assert.assertTrue(jedis.rpop("1") == null);
        jedis.close();
    }

    @Test
    public void testLockTimeout(){
        redisLockUtil.lock("lockTest1", "1", 10000);
    }

    @Test
    public void testBrop(){
       Jedis jedis = jedisPool.getResource();
       String s = jedis.rpop("123");
       jedis.close();
    }

}
