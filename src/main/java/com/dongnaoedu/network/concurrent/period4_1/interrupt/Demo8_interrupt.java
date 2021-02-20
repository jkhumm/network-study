package com.dongnaoedu.network.concurrent.period4_1.interrupt;

import java.util.concurrent.locks.LockSupport;

public class Demo8_interrupt {

    public static void main(String[] args) throws InterruptedException {
        Thread testThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    LockSupport.park();
                    System.out.println("here i am...");
                }
            }
        });


        testThread.start();

        Thread.sleep(1000L);
        testThread.interrupt();

        System.out.println("main end");

    }

}
