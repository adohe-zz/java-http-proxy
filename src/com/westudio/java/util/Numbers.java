package com.westudio.java.util;

/**
 * Created with IntelliJ IDEA.
 * User: tonyhe
 * Date: 14-5-26
 * Time: 下午6:35
 * To change this template use File | Settings | File Templates.
 */
public class Numbers {

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
