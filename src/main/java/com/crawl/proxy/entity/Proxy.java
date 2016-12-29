package com.crawl.proxy.entity;


import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

public class Proxy implements Delayed{
    private long delayTime;//任务间隔时间,单位ms
    private String ip;
    private int port;
    private boolean availableFlag;
    private boolean anonymousFlag;
    private long lastUseTime;
    private int delay;

    public Proxy() {
    }

    public Proxy(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    public Proxy(String ip, int port, long delayTime) {
        this.ip = ip;
        this.port = port;
        this.delayTime = delayTime;
        this.delayTime=TimeUnit.NANOSECONDS.convert(delayTime, TimeUnit.MILLISECONDS) + System.nanoTime();
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


    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(delayTime - System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        Proxy element = (Proxy)o;
        return delayTime > element.delayTime ? 1:(delayTime < element.delayTime ? -1 : 0);
    }

    @Override
    public String toString() {
        return "Proxy{" +
                "delayTime=" + delayTime +
                ", ip='" + ip + '\'' +
                ", port=" + port +
                ", availableFlag=" + availableFlag +
                ", anonymousFlag=" + anonymousFlag +
                ", lastUseTime=" + lastUseTime +
                ", delay=" + delay +
                '}';
    }
}
