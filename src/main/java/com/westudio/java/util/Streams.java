package com.westudio.java.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: nankonami
 * Date: 14-6-5
 * Time: 下午11:45
 * To change this template use File | Settings | File Templates.
 */
public class Streams {

    private static final int BUFFER_SIZE = 2048;

    public static void pipe(InputStream is, OutputStream os, boolean flush) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while ((bytesRead = is.read(buffer)) > 0) {
            os.write(buffer, 0, bytesRead);
            if (flush) {
                os.flush();
            }
        }
    }

    public static void pipe(InputStream is, OutputStream os) throws IOException {
        pipe(is, os, false);
    }
}
