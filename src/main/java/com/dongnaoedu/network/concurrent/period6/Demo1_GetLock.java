package com.dongnaoedu.network.concurrent.period6;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class Demo1_GetLock {
    //公平锁
    //static Lock lock = new ReentrantLock(true);

    //非公平锁
    static Lock lock = new ReentrantLock();

    public static void main(String args[]) throws InterruptedException {
        //主线程 拿到锁
        lock.lock();

        Thread th =  new Thread(new Runnable() {
            @Override
            public void run() {
                //子线程 获取锁(浅尝辄止)
/*                boolean result = lock.tryLock();
                System.out.println(result);
                System.out.println("tryLock done...");*/

                //子线程 获取锁(点到为止)
/*                try {
                    boolean result = lock.tryLock(5, TimeUnit.SECONDS);
                    System.out.println(result);
                    System.out.println("do not wait any more...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/

//子线程 获取锁（任人摆布）
/*                try {
                    System.out.println("start to get lock Interruptibly");
                    lock.lockInterruptibly();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("dad asked me to stop...");
                }*/

                //子线程 获取锁（不死不休）
                System.out.println("begain to get lock...");
                lock.lock();
                System.out.println("succeed to get lock...");

            }
        });
        th.start();

        Thread.sleep(4000L);
        System.out.println("main:interrupt child thread");
        th.interrupt();

        Thread.sleep(14000L);
        lock.unlock();

        Thread.sleep(7000L);

    }

}
