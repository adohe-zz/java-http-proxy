package com.westudio.java.socket.pool.exceptions;

public class SocketsException extends RuntimeException {

    public SocketsException(String msg) {
        super(msg);
    }

    public SocketsException(Throwable e) {
        super(e);
    }

    public SocketsException(String msg, Throwable e) {
        super(msg, e);
    }
}
