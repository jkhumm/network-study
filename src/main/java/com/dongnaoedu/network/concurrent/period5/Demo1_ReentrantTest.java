package com.dongnaoedu.network.concurrent.period5;


public class Demo1_ReentrantTest {

    private static  int i = 0;

    //可重入锁
    //private final static ReentrantLock lc = new ReentrantLock();
    private final static KodyLock lc = new KodyLock();


    public static void add() throws InterruptedException {
        lc.lock();

        i++;

        System.out.println("here i am...");
        Thread.sleep(1000L);
        add();


        lc.unlock();
    }


    public static void main(String args[]) throws InterruptedException {
        add();


    }
}
