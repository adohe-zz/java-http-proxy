package com.westudio.java.socket.pool.metrics;

public abstract class MetricCounter<T extends Number> extends Metric {

    public MetricCounter(String name, String description){
        super(name, description);
    }

    public abstract T value();
}
