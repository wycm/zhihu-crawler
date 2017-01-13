package com.crawl.core.util;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


public class SimpleInvocationHandler implements InvocationHandler{
    private static Logger logger = SimpleLogger.getSimpleLogger(HttpClientUtil.class);

    private Object target;

    public SimpleInvocationHandler() {
        super();
    }

    public SimpleInvocationHandler(Object target) {
        super();
        this.target = target;
    }
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//        logger.debug("++++++before " + method.getName() + "++++++");
        long startTime = System.currentTimeMillis();
        Object result = method.invoke(target, args);
        long endTime = System.currentTimeMillis();
//        logger.debug("++++++after " + method.getName() + "++++++");
        logger.debug(target.getClass().getSimpleName() + " " + method.getName() + " cost time:" + (endTime - startTime) + "ms");
        return result;
    }
}
