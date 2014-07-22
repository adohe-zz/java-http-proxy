package com.westudio.java.util;

import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Log {

    private static AtomicReference<Logger> logger_ =
            new AtomicReference<>(Logger.getGlobal());

    private static ThreadLocal<String> suffix_ = new ThreadLocal<>();

    private static ThreadLocal<Throwable> t_ = new ThreadLocal<>();

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

    public static Throwable getThrowable() {
        return t_.get();
    }

    public static void setThrowable(Throwable throwable) {
        t_.set(throwable);
    }

    public static void log(Level l, StackTraceElement source, String s, Throwable t) {
        String suffix = suffix_.get();
        logger_.get().logp(l, source.getClassName(), source.getMethodName(),
                suffix == null ? s : s + suffix, t == null ? null : t);
    }

    public static void log(Level l, String s, Throwable t) {
        log(l, new Throwable().getStackTrace()[2], s, t);
    }

    public static void v(String s) {

    }

    public static void v(Throwable t) {

    }

    public static void v(String s, Throwable t) {

    }

    public static void d(String s) {

    }

    public static void d(Throwable t) {

    }

    public static void d(String s, Throwable t) {

    }

    public static void i(String s) {

    }

    public static void i(Throwable t) {

    }

    public static void i(String s, Throwable t) {

    }

    public static void w(String s) {

    }

    public static void w(Throwable t) {

    }

    public static void w(String s, Throwable t) {

    }

    public static void e(String s) {

    }

    public static void e(Throwable t) {

    }

    public static void e(String s, Throwable t) {

    }
}
