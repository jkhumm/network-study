package com.dongnaoedu.network.concurrent.period4;

public class Counter {
    volatile int i = 0;

    public void add() {
        i = i + 1;
    }
}
