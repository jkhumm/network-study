package com.dongnaoedu.network.concurrent.period4;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

public class KodyLock implements Lock {
    //有一个变量代表谁或得了锁
    //private Thread owner = null;
    private AtomicReference<Thread> owner = new AtomicReference<>();

    private BlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();


    @Override
    public boolean tryLock() {
        //如果锁没有占用     存在原子性问题
        /*if (owner == null){
            owner = Thread.currentThread();
            return true;
        }else{
            return false;
        }*/

        return owner.compareAndSet(null, Thread.currentThread());
    }

    @Override
    public void lock() {
        while(!tryLock()){
            Thread curTh = Thread.currentThread();
            waiters.offer(curTh);
            LockSupport.park();
        }
    }

    @Override
    public void unlock() {
        if (owner.compareAndSet(Thread.currentThread(), null)){
            owner = null;
            LockSupport.unpark(waiters.poll());
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }



    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }



    @Override
    public Condition newCondition() {
        return null;
    }



}
