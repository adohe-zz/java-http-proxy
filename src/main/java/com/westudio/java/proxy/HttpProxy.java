package com.westudio.java.proxy;

import com.sun.net.httpserver.HttpServer;
import com.westudio.java.util.Conf;
import com.westudio.java.util.Numbers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicBoolean;

public class HttpProxy {

    private static AtomicBoolean running = new AtomicBoolean(true);

    private static final int DEFAULT_PORT = 3128;
    private static final int DEFAULT_BACKLOG = 50;

    public static void main(String[] args) {

        if (Conf.handleShutdown(HttpProxy.class, args, running)) {
            return;
        }

        int port = 0;
        String host = "127.0.0.1";

        if (args != null && args.length >= 1) {
            if (args.length  == 1) {
                port = Numbers.parseInt(args[0], DEFAULT_PORT);
            } else {
                host = args[0];
                port = Numbers.parseInt(args[1], DEFAULT_PORT);
            }
        }

        try {
            HttpServer httpServer = HttpServer.create(new InetSocketAddress(host, port), DEFAULT_BACKLOG);
            RequestHandler handler = new RequestHandler();
            httpServer.createContext("/", handler);
            httpServer.start();

            while (running.get()) {
                try {
                    Thread.sleep(16);
                } catch (InterruptedException e) {/**/}
            }

            httpServer.stop(0);
            // TODO:Close all sockets
        } catch (IOException e) {
        }
    }
}
