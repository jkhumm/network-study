package com.dongnaoedu.network.concurrent.period6.locks2;



import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {

    static Lock lc = new KodyLock();
    //static Lock lc = new ReentrantLock();


    static volatile int i = 0;
    public static void add() throws InterruptedException {
        lc.lock();

        i++;
        System.out.println("here im ..");
        Thread.sleep(1000L);
        add();

        lc.unlock();
    }

    public static void main(String args[]) throws InterruptedException {
        add();




    }



}
