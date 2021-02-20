package com.dongnaoedu.network.humm.多线程.ReenLock.AQS;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.IntStream;

/**
 * @author heian
 * @create 2020-03-07-9:08 下午
 * @description 计数器就是一把共享锁，只有当线程执行之后-1，直至0
 */
public class MyCountDownLatch {

    private Sync sync;

    public MyCountDownLatch(int count){
        if (count<0){
            throw new IllegalArgumentException("参数传入有误");
        }
        this.sync= new Sync(count);
    }

    //阻塞 只有当readCount == 0,释放共享锁,类比ReentrantLock的lock拿到锁
    public void await() {
        sync.acquireShared(1);
    }
    //countDown 就是释放锁，并把state-1
    public void countDown() {
        sync.releaseShared(1);
    }

    class Sync extends AbstractQueuedSynchronizer{
        //开始存值
        public Sync(int count){
            setState(count);
        }
        //await 就是抢占共享锁,当state变为0的时候就表示锁释放，就会去唤醒main或者抢锁线程抢锁
        @Override
        protected int tryAcquireShared(int arg) {
            return getState()<=0 ? 1:-1;
        }

        //countDown 释放锁就是-1，直至减少到0,才会释放共享锁
        @Override
        protected boolean tryReleaseShared(int arg) {
            //System.out.println("打印队列长度" + getQueueLength());
            for (;;){
                int newNum = getState() - arg;
                if (compareAndSetState(getState(),newNum)){
                    return newNum <= 0;
                }
            }
        }
    }




    public static void main(String[] args) {
        MyCountDownLatch countDownLatch = new MyCountDownLatch(5);
        IntStream.range(0,5).forEach(value -> {
            new Thread(() -> {
                LockSupport.parkNanos(1000000000*1L);
                System.out.println(Thread.currentThread().getName() + "结束");
                countDownLatch.countDown();
            }).start();
        });
        countDownLatch.await();
        System.out.println("main开始");

    }


}
