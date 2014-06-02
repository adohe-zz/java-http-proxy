package com.westudio.java.util.pool;

import com.westudio.java.util.pool.exception.SocketsException;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Created with IntelliJ IDEA.
 * User: tonyhe
 * Date: 14-5-28
 * Time: 下午6:30
 * To change this template use File | Settings | File Templates.
 */
public abstract class Pool<T> {

    protected GenericObjectPool<T> internalPool;

    public Pool() {
    }

    public Pool(PooledObjectFactory<T> factory, final GenericObjectPoolConfig config) {
        initPool(factory, config);
    }

    private void initPool(PooledObjectFactory<T> factory, final GenericObjectPoolConfig config) {

        if (this.internalPool != null) {
            try {
                closeInternalPool();
            } catch (Exception e) {
            }
        }

        this.internalPool = new GenericObjectPool<T>(factory, config);
    }

    public T getResource() {
        try {
            return internalPool.borrowObject();
        } catch (Exception e) {
            throw new SocketsException("Could not get a resource from the pool", e);
        }
    }

    public void returnResource(final T resource) {
        if (resource == null) {
            return;
        }

        try {
            internalPool.returnObject(resource);
        } catch (Exception e) {
            throw new SocketsException("Could not return the resource to the pool", e);
        }
    }

    public void destroy() {
        closeInternalPool();
    }

    protected void closeInternalPool() {
        try {
            internalPool.close();
        } catch (Exception e) {
            throw new SocketsException("Could not destroy the pool", e);
        }
    }
}
