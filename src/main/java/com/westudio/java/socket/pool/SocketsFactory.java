package com.westudio.java.socket.pool;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class SocketsFactory implements KeyedPooledObjectFactory<HostInfo, SocketConnection> {

	@Override
	public PooledObject<SocketConnection> makeObject(HostInfo key)
			throws Exception {
		SocketConnection socketConnection = new SocketConnection(key);
		socketConnection.connect();
		return new DefaultPooledObject<SocketConnection>(socketConnection);
	}

	@Override
	public void destroyObject(HostInfo key, PooledObject<SocketConnection> p)
			throws Exception {
		SocketConnection sockConn = p.getObject();
		sockConn.close();
	}

	@Override
	public boolean validateObject(HostInfo key, PooledObject<SocketConnection> p) {
		SocketConnection sockConn = p.getObject();
		return sockConn.isConnected();
	}

	@Override
	public void activateObject(HostInfo key, PooledObject<SocketConnection> p)
			throws Exception {
		SocketConnection sockConn = p.getObject();
		sockConn.connect();
	}

	@Override
	public void passivateObject(HostInfo key, PooledObject<SocketConnection> p)
			throws Exception {
	}

}
