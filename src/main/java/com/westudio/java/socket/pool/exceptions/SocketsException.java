package com.westudio.java.socket.pool.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: tonyhe
 * Date: 14-6-4
 * Time: 上午11:11
 * To change this template use File | Settings | File Templates.
 */
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
