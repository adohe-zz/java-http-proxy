package com.westudio.java.socket.pool;

import com.westudio.java.socket.pool.exceptions.SocketsException;
import com.westudio.java.util.Factory;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

public class SocketConnection implements Closeable {

	private HostInfo hostInfo;
	private Socket socket;
	private SocketOutputStream outputStream;
	private SocketInputStream inputStream;
	private int timeout = 15 * 1000;

    public SocketConnection(Socket socket, final HostInfo hostInfo) {
        super();
        if (socket == null) {
            throw new IllegalArgumentException("socket must not be null");
        }
        if (hostInfo == null) {
            throw new IllegalArgumentException("hostinfo must not be null");
        }

        this.socket = socket;
        this.hostInfo = hostInfo;
    }

	public SocketConnection(Socket socket, final HostInfo hostInfo, final int timeout) {
		this(socket, hostInfo);
		this.timeout = timeout;
	}

	public void adjustTimeout(final int timeout) throws IOException {
		if (timeout < 1) {
			setTimeoutInfinite();
		} else {
			if (!isConnected()) {
				connect();
			}
			socket.setSoTimeout(timeout);
		}
	}

	public void setTimeoutInfinite() throws IOException {
		if (!isConnected()) {
			connect();
		}
		socket.setKeepAlive(true);
		socket.setSoTimeout(0);
	}

	public void rollbackTimeout() throws SocketException {
		socket.setSoTimeout(timeout);
		socket.setKeepAlive(false);
	}

	protected void flush() throws IOException {
		outputStream.flush();
	}

	/**
	 * use these settings by default
	 *
	 * @throws IOException
	 */
	public void connect() {
		if (!isConnected()) {
            try {
                socket = Factory.getSocketFactory("https".equals(hostInfo.getSchema()) ? true : false).createSocket();

                socket.setReuseAddress(true);
                socket.setKeepAlive(true);
                socket.setTcpNoDelay(true);
                socket.setSoLinger(true, 0);

                socket.connect(new InetSocketAddress(hostInfo.getHostname(),
                        hostInfo.getPort()), timeout);
                socket.setSoTimeout(timeout);
                outputStream = new SocketOutputStream(socket.getOutputStream());
                inputStream = new SocketInputStream(socket.getInputStream());
            } catch (IOException e) {
                throw new SocketsException(e);
            }
		}
	}

	@Override
	public void close() throws IOException {
		disconnect();
	}

	public void disconnect() throws IOException {
		if (isConnected()) {
			inputStream.close();
			outputStream.close();
			if (!socket.isClosed()) {
				socket.close();
			}
		}
	}

	public boolean isConnected() {
		return socket != null && socket.isBound() && !socket.isClosed()
				&& socket.isConnected() && !socket.isInputShutdown()
				&& !socket.isOutputShutdown();
	}

	// public SockConnection(final String host) {
	// super();
	// this.host = host;
	// }
	// public String getHost() {
	// return host;
	// }
	// public void setHost(final String host) {
	// this.host = host;
	// }
	// public int getPort() {
	// return port;
	// }
	// public void setPort(final int port) {
	// this.port = port;
	// }
	public HostInfo getHostInfo() {
		return hostInfo;
	}

	public Socket getSocket() {
		return socket;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(final int timeout) {
		this.timeout = timeout;
	}
	@Override
	public String toString() {
		if (this.socket != null) {
            StringBuilder sb = new StringBuilder();
            SocketAddress remote = this.socket.getRemoteSocketAddress();
            SocketAddress local = this.socket.getLocalSocketAddress();
            if (remote != null||local != null) {
                formatAddress(sb, local);
                sb.append("<->");
                formatAddress(sb, remote);
            }
            return sb.toString();
        } else {
            return super.toString();
        }
	}

	private static void formatAddress(final StringBuilder sb, final SocketAddress socketAddress) {
        if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress addr = ((InetSocketAddress) socketAddress);
            sb.append(addr.getAddress() != null ? addr.getAddress().getHostAddress() :
                addr.getAddress())
            .append(':')
            .append(addr.getPort());
        } else {
        	sb.append(socketAddress);
        }
    }
}
