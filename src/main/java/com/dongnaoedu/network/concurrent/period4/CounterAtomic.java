package com.dongnaoedu.network.concurrent.period4;

import java.util.concurrent.atomic.AtomicInteger;

public class CounterAtomic {
    AtomicInteger at = new AtomicInteger(0);

    public void add(){
        at.getAndIncrement();
    }

    public int getValue(){
        return at.get();
    }
}
