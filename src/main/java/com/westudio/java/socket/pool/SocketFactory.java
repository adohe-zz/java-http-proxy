package com.westudio.java.socket.pool;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

public class SocketFactory implements KeyedPooledObjectFactory<HostInfo, SockConnection>{

	@Override
	public PooledObject<SockConnection> makeObject(HostInfo key)
			throws Exception {
		SockConnection sockConnection=new SockConnection(key);
		sockConnection.connect();
		return new DefaultPooledObject<SockConnection>(sockConnection);
	}

	@Override
	public void destroyObject(HostInfo key, PooledObject<SockConnection> p)
			throws Exception {
		SockConnection sockConn=p.getObject();
		sockConn.close();
	}

	@Override
	public boolean validateObject(HostInfo key, PooledObject<SockConnection> p) {
		SockConnection sockConn=p.getObject();
		return sockConn.isConnected();// FIXME not correct implementation!!!!!!
	}

	@Override
	public void activateObject(HostInfo key, PooledObject<SockConnection> p)
			throws Exception {
		SockConnection sockConn=p.getObject();
		sockConn.connect();// FIXME not correct implementation!!!!!!
	}

	@Override
	public void passivateObject(HostInfo key, PooledObject<SockConnection> p)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
