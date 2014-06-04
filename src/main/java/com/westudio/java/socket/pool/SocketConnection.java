package com.westudio.java.socket.pool;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;

public class SocketConnection implements Closeable {

	private HostInfo hostInfo;
	private Socket socket;
	private SocketOutputStream outputStream;
	private SocketInputStream inputStream;
	private int timeout = TempConstants.SOCKCONNETCTION_SOTIMEOUT;

	/*protected SockConnection() {

	}*/

	public SocketConnection(final HostInfo hostInfo) {
		super();
		if (null == hostInfo) {
			throw new IllegalArgumentException("hostInfo mnust not be null");
		}
		this.hostInfo = hostInfo;
	}

	public SocketConnection(final HostInfo hostInfo, final int timeout) {
		this(hostInfo);
		this.timeout=timeout;
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
	 * use these settings by default //FIXME
	 *
	 * @throws IOException
	 */
	public void connect() throws IOException {
		if (!isConnected()) {
			socket = new Socket();
			socket.setReuseAddress(true);
			socket.setKeepAlive(true);
			socket.setTcpNoDelay(true);
			socket.setSoLinger(true, 0);
			socket.connect(new InetSocketAddress(hostInfo.getHostname(),
					hostInfo.getPort()), timeout);
			socket.setSoTimeout(timeout);
			outputStream = new SocketOutputStream(socket.getOutputStream());
			inputStream = new SocketInputStream(socket.getInputStream());
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
}
