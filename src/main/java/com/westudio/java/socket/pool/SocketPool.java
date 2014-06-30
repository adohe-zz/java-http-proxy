package com.westudio.java.socket.pool;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

public final class SocketPool extends KeyedPool<HostInfo, SocketConnection> {

	Cache<String, HostInfo> cache = CacheBuilder.newBuilder().maximumSize(1000)
			.expireAfterWrite(30, TimeUnit.MINUTES).softValues().build();

    private final int PORT = 80;

    public SocketPool() {
    }

    public SocketPool(final GenericKeyedObjectPoolConfig config) {
        super(new SocketsFactory(), config);
    }

	@Override
	public HostInfo makeKey(SocketConnection resource) {
		return resource.getHostInfo();
	}

	@Override
	public HostInfo makeKey(String url) {

        try {
            URI uri = new URI(url);
            return makeKey(uri);
        } catch (URISyntaxException e) {
        }

        return null;
    }

    @Override
    public HostInfo makeKey(final URI uri) {
        final int port = (uri.getPort() == -1) ? PORT : uri.getPort();
        HostInfo hostInfo = null;
        try {
            hostInfo = cache.get(uri.getScheme() + "://" + uri.getHost() + ":" + port, new Callable<HostInfo>() {
                @Override
                public HostInfo call() throws Exception {
                    return new HostInfo(uri.getHost(), port);
                }
            });
        } catch (ExecutionException e) {
        }

        return hostInfo;
    }

    public static void main(String[] args) {
		System.out.println(URI.create("http://blog.csdn.net/kongxx/article/details/6612760").getPort());
	}
}
