package com.dongnaoedu.network.humm.多线程.ReenLock.AQS;

import java.util.concurrent.locks.LockSupport;
import java.util.stream.IntStream;

/**
 * @author heian
 * @create 2020-03-07-10:53 下午
 * @description
 */
public class MyCountDownLatch2 {

    private Sync sync;

    public MyCountDownLatch2(int count){
        if (count<0){
            throw new IllegalArgumentException("参数传入有误");
        }
        this.sync = new Sync(count);
    }


    class Sync extends MyAQS{
        public Sync(int count){
            readCount.compareAndSet(readCount.get(),count);
        }

        // await readCount<=0 >0 表示别的线程抢锁成功  <0 失败
        @Override
        public int tryLockShared(int acquires) {
            return readCount.get() <=0 ? 1:-1;
        }
        //countDown别的线程抢锁，只有当readCount ==0 才会抢成功
        @Override
        public boolean tryUnlockShared(int releases) {
            for (;;){
               int newNum = readCount.get() - releases;
               if (readCount.compareAndSet(readCount.get(),newNum)){
                   return readCount.get()<=0;
               }
            }
        }

    }

    public void await()  {
        //我的AQS这里arg 默认是1  就没传参输
        this.sync.lockShared();
    }

    public void countDown() {
        //我的AQS这里arg 默认是1  就没传参输
        this.sync.unlockShared();
    }


    public static void main(String[] args) {
        MyCountDownLatch2 countDownLatch = new MyCountDownLatch2(5);
        IntStream.range(0,20).forEach(value -> {
            new Thread(() -> {
                LockSupport.parkNanos(1000000000*3L);
                System.out.println(Thread.currentThread().getName() + "结束");
                countDownLatch.countDown();
            }).start();
        });
        countDownLatch.await();
        System.out.println("main开始");

    }



}
