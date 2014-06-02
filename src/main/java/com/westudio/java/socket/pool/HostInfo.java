package com.westudio.java.socket.pool;

import java.io.Serializable;

public class HostInfo implements Cloneable, Serializable {
	private static final long serialVersionUID = -8530154880698910845L;
	private final String hostname;
	private final int port;
	//private final String schemeName;
	public final static int DEFAULT_PORT=80;
	//public static final String DEFAULT_SCHEME_NAME = "http";//e.f. jdbc:mysql
	public HostInfo(String hostname, int port) {
		super();
		this.hostname = hostname;
		this.port = port;
	}
	public HostInfo(String hostname) {
		this(hostname,DEFAULT_PORT);
	}
	public String getHostname() {
		return hostname;
	}
	public int getPort() {
		return port;
	}
	@Override
    public boolean equals(final Object obj) {
        if (this == obj) return true;
        if (obj instanceof HostInfo) {
        	HostInfo that = (HostInfo) obj;
            return this.hostname.equalsIgnoreCase(that.hostname)
                && this.port == that.port;
        } else {
            return false;
        }
    }
	@Override
    public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(this.hostname);
        if (this.port>0&&80!=this.port) {
        	sb.append(':');
        	sb.append(Integer.toString(this.port));
        }
        return sb.toString();
    }
}
