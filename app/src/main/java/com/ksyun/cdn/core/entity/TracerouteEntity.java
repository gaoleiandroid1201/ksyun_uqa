/*
This file is part of the project TraceroutePing, which is an Android library
implementing Traceroute with ping under GPL license v3.
Copyright (C) 2013  Olivier Goutay

TraceroutePing is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

TraceroutePing is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with TraceroutePing.  If not, see <http://www.gnu.org/licenses/>.
 */

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
