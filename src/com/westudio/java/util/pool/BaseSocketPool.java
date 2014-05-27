package com.westudio.java.util.pool;

/**
 * Created with IntelliJ IDEA.
 * User: tonyhe
 * Date: 14-5-27
 * Time: 下午4:21
 * To change this template use File | Settings | File Templates.
 */
public abstract class BaseSocketPool<T> implements Pool<T> {

    private volatile boolean closed = false;

    class Entry {
        T obj;
        long timeout;

        Entry(T obj, long timeout) {
            obj = obj;
            timeout = timeout;
        }
    }

    @Override
    public abstract T makeObject();

    @Override
    public abstract void activateObject(T obj);

    @Override
    public abstract void passivateObject(T obj);

    @Override
    public abstract void destroyObject(T obj);

    @Override
    public void close() {
    }

    @Override
    public T borrowObject() {
        return null;
    }

    @Override
    public void returnObject(T obj) {
    }
}
