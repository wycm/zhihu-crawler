package com.github.wycm.common;

import com.alibaba.fastjson.JSONObject;
import com.github.wycm.common.util.Constants;
import com.github.wycm.common.util.SimpleHttpClient;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;

/**
 * Created by wycm on 2019-01-20.
 */
@Service
@Slf4j
public class LocalIPService {

    @Getter
    private volatile static String localIp = "";

    public final static int DIRECT_CONNECT_PORT = -1;

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private ProxyQueue proxyQueue;

    @Autowired
    private  CommonProperties commonProperties;

    @PostConstruct
    public void initIp(){
        try {
            String s = new SimpleHttpClient(jedisPool).get("http://www.httpbin.org/ip");
            this.localIp = JSONObject.parseObject(s).getString("origin");
            Proxy proxy = new Proxy(localIp, DIRECT_CONNECT_PORT, Constants.TIME_INTERVAL);
            proxy.setLastUseTime(proxyQueue.getRedisServerTime());
            proxyQueue.addProxy(commonProperties.getProxyPageProxyQueueName(), proxy);
            proxyQueue.addProxy(commonProperties.getTargetPageProxyQueueName(), proxy);
        } catch (ExecutionException e) {
            log.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        } catch (Exception e){
            log.error("init ip failed", e);
        }
    }
}
