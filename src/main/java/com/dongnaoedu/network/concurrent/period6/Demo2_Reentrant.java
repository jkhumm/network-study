package com.dongnaoedu.network.concurrent.period6;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demo2_Reentrant {
    static Lock lock =  new ReentrantLock();

    public static void main(String args[]) throws InterruptedException {
        System.out.println("get lock 1...");
        lock.lock();    //当前线程已获取锁

        System.out.println("get lock 2...");
        lock.lock();    //再次获取，是否能成功

        //打印消息，说明再次获取锁成功
        System.out.println("here I am ...");


        System.out.println("unlock 1...");
        lock.unlock();

        lock.unlock();


        new Thread(){
            @Override
            public void run() {
                lock.lock();

                System.out.println("子线程获得所。。。");

                lock.unlock();
            }
        }.start();


/*        System.out.println("unlock 2...");
        lock.unlock();

        Thread.sleep(1000L);
        System.out.println("unlock 3...");
        lock.unlock();*/
    }
}
