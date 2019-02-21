package com.github.wycm.proxy.util;


import com.github.wycm.common.Proxy;

public class ProxyUtil {
    /**
     * 是否丢弃代理
     * 连续失败5次，且失败率超过40%，丢弃
     */

    public static boolean isDiscardProxy(Proxy proxy){
        int succTimes = proxy.getSuccessfulTimes();
        int failTimes = proxy.getFailureTimes();
        int ConFailTimes = proxy.getContinuousFailureTimes();
        if(ConFailTimes >= 5){
            double failRate = (failTimes + 0.0) / (succTimes + failTimes);
            if (failRate > 0.5){
                return true;
            }
        }
        return false;
    }

    public static void handleResponseSuccProxy(Proxy currentProxy){
        if (currentProxy != null){
            currentProxy.setSuccessfulTimes(currentProxy.getSuccessfulTimes() + 1);
            currentProxy.setContinuousFailureTimes(0);
            currentProxy.setSuccessfulTotalTime(0);
            double aTime = (currentProxy.getSuccessfulTotalTime() + 0.0) / currentProxy.getSuccessfulTimes();
            currentProxy.setSuccessfulAverageTime(aTime);
            currentProxy.setLastSuccessfulTime(System.currentTimeMillis());
        }
    }

    public static void handleResponseFailedProxy(Proxy currentProxy){
        if(currentProxy != null){
            currentProxy.setFailureTimes(currentProxy.getFailureTimes() + 1);
            currentProxy.setContinuousFailureTimes(currentProxy.getContinuousFailureTimes() + 1);
        }
    }

}
