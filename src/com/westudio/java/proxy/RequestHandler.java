package com.westudio.java.proxy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.westudio.java.util.pool.SocketPool;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: tonyhe
 * Date: 14-5-26
 * Time: 下午6:41
 * To change this template use File | Settings | File Templates.
 */
public class RequestHandler implements HttpHandler {

    private ConcurrentHashMap<String, SocketPool> poolMap;

    public RequestHandler() {
        poolMap = new ConcurrentHashMap<String, SocketPool>();
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

    }
}
