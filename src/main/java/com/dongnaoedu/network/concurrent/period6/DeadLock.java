package com.dongnaoedu.network.concurrent.period6;

import java.util.concurrent.locks.LockSupport;

public class DeadLock {

    static String lock = "";

    public static void main(String args[]) throws InterruptedException {

       Thread th1 = new Thread(){
            @Override
            public void run() {
                synchronized (lock){
                    System.out.println("子线程here1");
                    LockSupport.park();
                    System.out.println("子线程here2");
                }
            }
        };
       th1.start();


        Thread.sleep(3000L);

        synchronized (lock){
            System.out.println("主线程here1");
            LockSupport.unpark(th1);
        }

    }


}
