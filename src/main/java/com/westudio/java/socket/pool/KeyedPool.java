package com.westudio.java.socket.pool;

import com.westudio.java.socket.pool.exceptions.SocketsException;
import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.impl.GenericKeyedObjectPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import java.net.URI;


/**
 * An adaptor of GenericKeyedObjectPool
 */
public abstract class KeyedPool<K, T> {

	protected GenericKeyedObjectPool<K, T> internalPool;

	public KeyedPool() {
	}

	/**
	 * @param factory
	 * @param config
	 */
	public KeyedPool(KeyedPooledObjectFactory<K, T> factory, final GenericKeyedObjectPoolConfig config) {
		initPool(factory, config);
	}

	private void initPool(KeyedPooledObjectFactory<K, T> factory, final GenericKeyedObjectPoolConfig config) {

		if (this.internalPool != null) {
			try {
				closeInternalPool();
			} catch (Exception e) {
			}
		}
		this.internalPool = new GenericKeyedObjectPool<K, T>(factory, config);
		//FIXME a better way to initiate see Effective JAVA...？
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				KeyedPool.this.destroy();
			}
		});
	}

	public T getResource(K key) {
        try {
            return internalPool.borrowObject(key);
        } catch (Exception e) {
            throw new SocketsException("could not get a resource from the pool.", e);
        }
    }

	public T getResource(String uri) {
        try {
            return internalPool.borrowObject(makeKey(uri));
        } catch (Exception e) {
            throw new SocketsException("could not get a resource from the pool.", e);
        }
    }

	public void returnResource(final T resource) {
		if (resource == null) {
			return;
		}
		K key = makeKey(resource);
		internalPool.returnObject(key, resource);
	}

	public abstract K makeKey(T resource);

	public abstract K makeKey(String url);

    public abstract K makeKey(URI uri);

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
