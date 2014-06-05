package com.westudio.java.proxy;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.westudio.java.socket.pool.HostInfo;
import com.westudio.java.socket.pool.SocketConnection;
import com.westudio.java.socket.pool.SocketPool;
import com.westudio.java.util.Numbers;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: tonyhe
 * Date: 14-5-26
 * Time: 下午6:41
 * To change this template use File | Settings | File Templates.
 */
public class RequestHandler implements HttpHandler {

    private SocketPool socketPool = new SocketPool(new GenericKeyedObjectPoolConfig());

    private static HashSet<String> SKIP_HEADERS = new HashSet<>(Arrays.asList(
            "ACCESS-CONTROL-REQUEST-METHODS",
            "ACCESS-CONTROL-REQUEST-HEADERS",
            "CONNECTION",
            "CONTENT-LENGTH",
            "CONTENT-TRANSFER-ENCODING",
            "KEEP-ALIVE",
            "TRAILER",
            "TRANSFER-ENCODING",
            "UPGRADE",
            "VIA",
            "X-FORWARDED-FOR",
            "X-FORWARDED-PROTO",
            "X-PKCS7-CERTIFICATES-BASE64",
            "DNT"
    ));
    public RequestHandler() {
    }

    private static void write(OutputStream ops, String str) throws IOException {
        ops.write(str.getBytes(StandardCharsets.ISO_8859_1));
    }

    private static void writeln(OutputStream ops) throws IOException {
        ops.write('\r');
        ops.write('\n');
    }

    private static void writeHeader(OutputStream ops, String key, String value) throws IOException {
        write(ops, key);
        ops.write(':');
        ops.write(' ');
        write(ops, value);
        writeln(ops);
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
            // Add request header
            BufferedOutputStream outputStream = new BufferedOutputStream(socketConnection.getSocket().getOutputStream());
            write(outputStream, method);
            outputStream.write(' ');
            write(outputStream, path);
            write(outputStream, " HTTP/1.1");
            writeln(outputStream);

            boolean withHost = false;
            int contentLength = -1;
            for (Map.Entry<String, List<String>> entry : httpExchange.getRequestHeaders().entrySet()) {
                String key = entry.getKey();

                if (key.equalsIgnoreCase("Host")) {
                    withHost = true;
                }

                if (SKIP_HEADERS.contains(key.toUpperCase())) {
                    if (key.equalsIgnoreCase("Content-Length")) {
                        List<String> value = entry.getValue();
                        if (!value.isEmpty()) {
                            contentLength = Numbers.parseInt(value.get(0));
                        }
                    }
                    continue;
                }

                for (String value : entry.getValue()) {
                    if (key.equalsIgnoreCase("Destination")) {
                        if ("https".equals(schema) && value.startsWith("https")) {
                            value = "http:" + value.substring(6);
                        }
                    }

                }
            }
        } catch (IOException e) {

        }
    }
}
