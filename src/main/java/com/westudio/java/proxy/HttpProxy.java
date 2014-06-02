package com.westudio.java.proxy;

import com.sun.net.httpserver.HttpServer;
import com.westudio.java.util.Numbers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created with IntelliJ IDEA.
 * User: tonyhe
 * Date: 14-5-26
 * Time: 下午5:47
 * To change this template use File | Settings | File Templates.
 */
public class HttpProxy {

    private static AtomicBoolean running = new AtomicBoolean(true);

    public static void main(String[] args) {

        String host = "127.0.0.1";
        int port = 3128;
        int backlog = 50;

        if (args != null && args.length >= 1) {
            if (args.length  == 1) {
                port = Numbers.parseInt(args[0], port);
            } else {
                host = args[0];
                port = Numbers.parseInt(args[1], port);
            }
        }

        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(host, port), backlog);
            RequestHandler handler = new RequestHandler();
            httpServer.createContext("/", handler);
            httpServer.start();

            while (running.get()) {
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            httpServer.stop(0);
            // TODO:Close all sockets
        } catch (IOException e) {
        }
    }
}
