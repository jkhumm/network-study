package com.dongnaoedu.network.concurrent.period6;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demo3_Condition2 {
    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("子线程或得锁...\n");
                lock.lock();
                try {
                    System.out.println("开始wait...\n");
                    Thread.sleep(6000L);
                    condition.await();
                    System.out.println("main unlock后，wait才结束...\n");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });


        th.start();
        Thread.sleep(3000L);

        lock.lock();
        condition.signal();

        //Thread.sleep(3000L);
        lock.unlock();
        System.out.println("main unlock...\n");
    }
}
