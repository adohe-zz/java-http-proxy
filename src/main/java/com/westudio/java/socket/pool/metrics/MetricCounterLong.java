package com.westudio.java.socket.pool.metrics;


public class MetricCounterLong extends MetricCounter<Long> {
    private final long value;

    MetricCounterLong(String name, String description, long value){
        super(name, description);
        this.value = value;
    }

    public Long value() {
        return value;
    }
}
