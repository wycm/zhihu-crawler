package com.crawl.proxy.entity;


import java.io.Serializable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Proxy implements Delayed, Serializable{
    private long timeInterval ;//任务间隔时间,单位ms
    private String ip;
    private int port;
    private boolean availableFlag;
    private boolean anonymousFlag;
    private long lastUseTime;
    private int delay;
    private int failureTimes;//请求失败次数
    private int successfulTimes;//请求成功次数

    public Proxy(String ip, int port, long timeInterval) {
        this.ip = ip;
        this.port = port;
        this.timeInterval = timeInterval;
        this.timeInterval=TimeUnit.NANOSECONDS.convert(timeInterval, TimeUnit.MILLISECONDS) + System.nanoTime();
    }
    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isAvailableFlag() {
        return availableFlag;
    }

    public void setAvailableFlag(boolean availableFlag) {
        this.availableFlag = availableFlag;
    }

    public boolean isAnonymousFlag() {
        return anonymousFlag;
    }

    public void setAnonymousFlag(boolean anonymousFlag) {
        this.anonymousFlag = anonymousFlag;
    }

    public long getLastUseTime() {
        return lastUseTime;
    }

    public void setLastUseTime(long lastUseTime) {
        this.lastUseTime = lastUseTime;
    }

    public int getDelay() {
        return delay;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setTimeInterval(long timeInterval){
        this.timeInterval=TimeUnit.NANOSECONDS.convert(timeInterval, TimeUnit.MILLISECONDS) + System.nanoTime();
    }
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(timeInterval - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        Proxy element = (Proxy)o;
        return timeInterval > element.timeInterval ? 1:(timeInterval < element.timeInterval ? -1 : 0);
    }

    public int getFailureTimes() {
        return failureTimes;
    }

    public void setFailureTimes(int failureTimes) {
        this.failureTimes = failureTimes;
    }

    public int getSuccessfulTimes() {
        return successfulTimes;
    }

    public void setSuccessfulTimes(int successfulTimes) {
        this.successfulTimes = successfulTimes;
    }

    @Override
    public String toString() {
        return "Proxy{" +
                "timeInterval=" + timeInterval +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", availableFlag=" + availableFlag +
                ", anonymousFlag=" + anonymousFlag +
                ", lastUseTime=" + lastUseTime +
                ", delay=" + delay +
                ", failureTimes=" + failureTimes +
                ", successfulTimes=" + successfulTimes +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Proxy proxy = (Proxy) o;

        if (port != proxy.port) return false;
        return ip.equals(proxy.ip);

    }

    @Override
    public int hashCode() {
        int result = ip.hashCode();
        result = 31 * result + port;
        return result;
    }

    public String getProxyStr(){
        return ip + ":" + port;
    }
}
