package com.westudio.java.socket.pool;

import com.westudio.java.socket.pool.exceptions.SocketsException;
import com.westudio.java.util.Factory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;

public class SocketConnection implements Closeable {

	private HostInfo hostInfo;
	private Socket socket;
	private OutputStream outputStream;
	private InputStream inputStream;
	private int timeout = 15 * 1000;

    private boolean broken = false;

    public SocketConnection() {
    }

    public SocketConnection(final HostInfo hostInfo) {
        super();
        if (hostInfo == null) {
            throw new IllegalArgumentException("HostInfo must not be null");
        }

        this.hostInfo = hostInfo;
    }

	public SocketConnection(final HostInfo hostInfo, final int timeout) {
        this(hostInfo);
		this.timeout = timeout;
	}

	public void adjustTimeout(final int timeout) {
        try {
            if (timeout < 1) {
                setTimeoutInfinite();
            } else {
                if (!isConnected()) {
                    connect();
                }

                socket.setSoTimeout(timeout);
            }
        } catch (SocketException e) {
            broken = true;
            throw new SocketsException(e);
        }
    }

	public void setTimeoutInfinite() {
        try {
            if (!isConnected()) {
                connect();
            }
            socket.setKeepAlive(true);
            socket.setSoTimeout(0);
        } catch (SocketException e) {
            broken = true;
            throw new SocketsException(e);
        }
	}

	public void rollbackTimeout() {
        try {
            socket.setSoTimeout(timeout);
            socket.setKeepAlive(false);
        } catch (SocketException e) {
            broken = true;
            throw new SocketsException(e);
        }
	}

	protected void flush() {
        try {
            outputStream.flush();
        } catch (IOException e) {
            broken = true;
            throw new SocketsException(e);
        }
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
                outputStream = socket.getOutputStream();
                inputStream = socket.getInputStream();
            } catch (IOException e) {
                broken = true;
                throw new SocketsException(e);
            }
		}
	}

	@Override
	public void close() {
		disconnect();
	}

	public void disconnect() {
        if (isConnected()) {
            try {
                inputStream.close();
                outputStream.close();
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                broken = true;
                throw new SocketsException(e);
            }
        }
	}

	public boolean isConnected() {
		return socket != null && socket.isBound() && !socket.isClosed()
				&& socket.isConnected() && !socket.isInputShutdown()
				&& !socket.isOutputShutdown();
	}

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

    public boolean getBroken() {
        return broken;
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
