package com.westudio.java.util.pool;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;

/**
 * Created with IntelliJ IDEA.
 * User: tonyhe
 * Date: 14-5-29
 * Time: 上午10:49
 * To change this template use File | Settings | File Templates.
 */
public class SocketFactory implements PooledObjectFactory<PooledSocket> {

    private final String host;
    private final int port;
    private final int timeout;

    public SocketFactory(final String host, final int port, final int timeout) {
        super();
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    @Override
    public PooledObject<PooledSocket> makeObject() throws Exception {
        return null;
    }

    @Override
    public void destroyObject(PooledObject<PooledSocket> pooledSocketPooledObject) throws Exception {
    }

    @Override
    public boolean validateObject(PooledObject<PooledSocket> pooledSocketPooledObject) {
        return false;
    }

    @Override
    public void activateObject(PooledObject<PooledSocket> pooledSocketPooledObject) throws Exception {
    }

    @Override
    public void passivateObject(PooledObject<PooledSocket> pooledSocketPooledObject) throws Exception {
    }
}
