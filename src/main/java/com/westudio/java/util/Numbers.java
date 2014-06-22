package com.westudio.java.util;

public class Numbers {

    public static int parseInt(String str) {
        return parseInt(str, 0);
    }

    public static int parseInt(String str, int i) {
        if (str == null) {
            return i;
        }

        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return i;
        }
    }
}
