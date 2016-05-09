
package com.ksyun.cdn.core.entity;

import java.io.Serializable;

public class TracerouteEntity implements Serializable {

	private static final long serialVersionUID = 1034744411998219581L;

	private String ip;
	private float ms;
	private int ttl;

	public TracerouteEntity(String ip, float ms, int ttl) {
		this.ip = ip;
		this.ms = ms;
		this.ttl = ttl;
	}

	public int getTtl() {
		return ttl;
	}

	public void setTtl(int ttl) {
		this.ttl = ttl;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public float getMs() {
		return ms;
	}

	public void setMs(float ms) {
		this.ms = ms;
	}

	@Override
	public String toString() {
		String f = "ttl: %2d,  ip: %18s, delay: %.3f ms";
		return String.format(f, ttl, ip, ms);
	}

	public void print() {
		System.out.println(toString());
	}
	
	public String toJson(){
		String f = "{'ttl': %d, 'ip': '%s', 'delay': %.3f}";
		return String.format(f, ttl, ip, ms);
	}

}
