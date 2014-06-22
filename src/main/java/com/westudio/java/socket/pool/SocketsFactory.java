package com.westudio.java.socket.pool;

import org.apache.commons.pool2.KeyedPooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;
import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SocketsFactory implements KeyedPooledObjectFactory<HostInfo, SocketConnection> {

    private static SocketFactory secureFactory;
    private static SocketFactory plainFactory;

    static {
        plainFactory = SocketFactory.getDefault();
        try {
            SSLContext ssl = SSLContext.getInstance("TLS");
            ssl.init(new KeyManager[0], new X509TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        }

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            }, null);
            secureFactory = ssl.getSocketFactory();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

	@Override
	public PooledObject<SocketConnection> makeObject(HostInfo key)
			throws Exception {
		SocketConnection socketConnection = new SocketConnection(null, key);
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
		return sockConn.isConnected();// FIXME not correct implementation!!!!!!
	}

	@Override
	public void activateObject(HostInfo key, PooledObject<SocketConnection> p)
			throws Exception {
		SocketConnection sockConn = p.getObject();
		sockConn.connect();// FIXME not correct implementation!!!!!!
	}

	@Override
	public void passivateObject(HostInfo key, PooledObject<SocketConnection> p)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
