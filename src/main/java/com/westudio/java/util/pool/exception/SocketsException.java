package com.westudio.java.util.pool.exception;

/**
 * Created with IntelliJ IDEA.
 * User: tonyhe
 * Date: 14-5-29
 * Time: 上午10:25
 * To change this template use File | Settings | File Templates.
 */
public class SocketsException extends RuntimeException {

    public SocketsException() {
    }

    public SocketsException(String msg) {
        super(msg);
    }

    public SocketsException(Throwable throwable) {
        super(throwable);
    }

    public SocketsException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
}
