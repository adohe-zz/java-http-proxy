package com.westudio.java.socket.pool;

import java.net.URI;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

public final class SocketPool extends KeyedPool<HostInfo, SocketConnection> {

	Cache<String, HostInfo> cache = CacheBuilder.newBuilder().maximumSize(1000)
			.expireAfterWrite(30, TimeUnit.MINUTES).softValues().build();

	@Override
	public HostInfo makeKey(SocketConnection resource) {
		return resource.getHostInfo();
	}

	@Override
	public HostInfo makeKey(String url) {
		final URI uri = URI.create(url);// FIXME
		final int port=(-1==uri.getPort())?TempConstants.SOCKCONNETCTION_PORT:uri.getPort();
		HostInfo hinfo = null;
		try {
			hinfo = cache.get(uri.getHost() + ":" + uri.getPort(),
					new Callable<HostInfo>() {
						@Override
						public HostInfo call() throws Exception {
							return new HostInfo(uri.getHost(),uri.getPort());
						}
					});
		} catch (ExecutionException e) {
		}
		return hinfo;
	}

	public static void main(String[] args) {
		System.out.println(URI.create("http://blog.csdn.net/kongxx/article/details/6612760").getPort());
	}
}
