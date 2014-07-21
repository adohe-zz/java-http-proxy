package com.westudio.java.util;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    private static AtomicReference<Logger> logger_ =
            new AtomicReference<>(Logger.getGlobal());
    private static ThreadLocal<String> suffix_ = new ThreadLocal<>();

    static {
        Logger.getGlobal().setLevel(Level.INFO);
    }

    public static Logger getAndSet(Logger logger) {
        return logger_.getAndSet(logger);
    }

    public static String getSuffix() {
        return suffix_.get();
    }

    public static void setSuffix(String suffix) {
        suffix_.set(suffix);
    }

    public static void log(Level l, StackTraceElement source, String s, Throwable t) {

    }
}
