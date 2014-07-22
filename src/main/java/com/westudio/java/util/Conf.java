package com.westudio.java.util;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.*;

public class Conf {

    private static boolean DEBUG = false;

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

    public static Logger openLogger(String name, int limit, int count) {
        Logger logger = Logger.getAnonymousLogger();
        logger.setLevel(Level.INFO);
        logger.setUseParentHandlers(false);

        if (DEBUG) {
            ConsoleHandler consoleHandler = new ConsoleHandler();
            consoleHandler.setLevel(Level.INFO);
            logger.addHandler(consoleHandler);
        }

        FileHandler fileHandler;
        try {
            String pattern = "";
            fileHandler = new FileHandler(pattern, limit, count, true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.INFO);
            logger.addHandler(fileHandler);
        } catch (IOException e) {
        }

        return logger;
    }

    public static void closeLogger(Logger logger) {
        // Close the logger
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
            handler.close();
        }
    }
}
