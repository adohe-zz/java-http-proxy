package com.westudio.java.proxy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.westudio.java.socket.pool.HostInfo;
import com.westudio.java.socket.pool.SocketConnection;
import com.westudio.java.socket.pool.SocketPool;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * Created with IntelliJ IDEA.
 * User: tonyhe
 * Date: 14-5-26
 * Time: 下午6:41
 * To change this template use File | Settings | File Templates.
 */
public class RequestHandler implements HttpHandler {

    private SocketPool socketPool = new SocketPool(new GenericKeyedObjectPoolConfig());

    public RequestHandler() {
    }

    private static void write(OutputStream ops, String str) throws IOException {
        ops.write(str.getBytes(StandardCharsets.ISO_8859_1));
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        URI uri = httpExchange.getRequestURI();
        String schema = uri.getScheme();
        String host = uri.getHost();
        int port = uri.getPort();
        port = port < 0 ? ("https".equals(schema) ? 443 : 80) : port;

        String query = uri.getQuery();
        String path = uri.getPath();
        String method = httpExchange.getRequestMethod();

        SocketConnection socketConnection;
        try {
            socketConnection = socketPool.getResource(new HostInfo(host, port));
            OutputStream outputStream = socketConnection.getSocket().getOutputStream();
            write(outputStream, method);

        } catch (IOException e) {

        }
    }
}
