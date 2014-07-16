package com.westudio.java.util;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.concurrent.atomic.AtomicBoolean;

public class Conf {

    public static boolean handleShutdown(Class<?> clazz, String[] args,
            final AtomicBoolean running) {
        if (args != null && args.length > 0 && args[0].equals("stop")) {
            running.set(false);
            return true;
        }

        SignalHandler signalHandler = new SignalHandler() {
            @Override
            public void handle(Signal signal) {
                running.set(false);
            }
        };

        String command = System.getProperty("sun.java.command");
        if (command == null || command.isEmpty()) {
            return false;
        }

        if (clazz.getName().equals(command.split(" ")[0])) {
            Signal.handle(new Signal("INT"), signalHandler);
            Signal.handle(new Signal("TERM"), signalHandler);
        }

        return false;
    }
}
