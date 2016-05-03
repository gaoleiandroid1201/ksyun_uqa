package com.ksyun.cdn.core.entity;

/**
 * Created by Administrator on 2016/3/22.
 */
public class TracerouteContainer {
    String hostname;
    String ip;
    float elapsedTime;

    public TracerouteContainer(String ip, float elapsedTime) {
        this.ip = ip;
        this.elapsedTime = elapsedTime;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getIp() {
        return ip;
    }
}
