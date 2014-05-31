package com.westudio.java.util.pool;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.net.URI;

/**
 * Created with IntelliJ IDEA.
 * User: tonyhe
 * Date: 14-5-29
 * Time: 上午10:43
 * To change this template use File | Settings | File Templates.
 */
public class SocketPool extends Pool<PooledSocket> {

    public SocketPool(final GenericObjectPoolConfig config, final String host) {

    }

    public SocketPool(String host, int port) {

    }

    public SocketPool(final String host) {

    }

    public SocketPool(final URI uri) {

    }

    public SocketPool(final GenericObjectPoolConfig config, final String host, final int port) {

    }

    public SocketPool(final GenericObjectPoolConfig config, final String host, int port,
                      int timeout) {

    }
}
