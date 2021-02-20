package com.dongnaoedu.network.concurrent.period6;

import java.util.concurrent.locks.LockSupport;

public class DeadLock2 {


    static String lock = "";

    public static void main(String args[]) throws InterruptedException {

        Thread th1 = new Thread(){
            @Override
            public void run() {
                synchronized (DeadLock2.class){
                    try {
                        Thread.sleep(7000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("子线程here1");
                    try {

                        DeadLock2.class.wait();    //会将锁释放
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("子线程here2");
                }
            }
        };
        th1.start();


        Thread.sleep(3000L);

        synchronized (DeadLock2.class){
            System.out.println("notify...");
            DeadLock2.class.notify();

        }

    }


}
