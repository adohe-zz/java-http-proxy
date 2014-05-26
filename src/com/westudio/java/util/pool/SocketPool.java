package com.westudio.java.util.pool;

import java.net.Socket;

/**
 * Created with IntelliJ IDEA.
 * User: tonyhe
 * Date: 14-5-26
 * Time: 下午7:05
 * To change this template use File | Settings | File Templates.
 */
public class SocketPool implements Pool<Socket> {

    @Override
    public Socket makeObject() {
        return null;
    }
}
