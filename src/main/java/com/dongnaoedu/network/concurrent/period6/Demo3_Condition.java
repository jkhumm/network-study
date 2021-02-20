package com.dongnaoedu.network.concurrent.period6;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demo3_Condition {
    private static Lock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    public static void main(String[] args) throws InterruptedException {

        Thread th = new Thread(new Runnable() {
            @Override
            public void run() {
                lock.lock();
                System.out.println("子线程或得锁...\n");
                try {
                    System.out.println("开始wait...\n");

                    condition.await();      // waiting  park

                    System.out.println("唤醒了...\n");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        });

        th.start();


        Thread.sleep(4000L);

        lock.lock();

        System.out.println("唤醒子线程...");
        condition.signal();

        Thread.sleep(10000L);
        lock.unlock();
        //System.out.println("main unlock...\n");
    }

}
