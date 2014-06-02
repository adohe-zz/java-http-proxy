package com.westudio.java.util.pool;

import com.westudio.java.util.Constants;
import com.westudio.java.util.pool.exception.SocketsException;

import java.io.Closeable;
import java.io.IOException;
import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: tonyhe
 * Date: 14-5-29
 * Time: 上午10:42
 * To change this template use File | Settings | File Templates.
 */
public class PooledSocket implements Closeable {

    private String host;
    private Socket socket;
    private int port = Constants.DEFAULT_PORT;
    private int timeout = Constants.DEFAULT_TIMEOUT;

    public PooledSocket() {
    }

    public PooledSocket(final String host) {
        super();
        this.host = host;
    }

    public PooledSocket(final String host, final int port) {
        super();
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Socket getSocket() {
        return socket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    @Override
    public void close() throws IOException {
        disconnect();
    }

    public void disconnect() {
        if (isConnected()) {
            try {
                if (!socket.isClosed()) {
                    socket.close();
                }
            } catch (IOException e) {
                throw new SocketsException(e);
            }
        }
    }

    public boolean isConnected() {
        return socket != null && socket.isBound() && !socket.isClosed()
                && socket.isConnected() && !socket.isInputShutdown()
                && !socket.isOutputShutdown();
    }
}
