package com.westudio.java.util.pool;

/**
 * Created with IntelliJ IDEA.
 * User: tonyhe
 * Date: 14-5-26
 * Time: 下午7:04
 * To change this template use File | Settings | File Templates.
 */
public interface Pool<T> {

    T makeObject();

}
