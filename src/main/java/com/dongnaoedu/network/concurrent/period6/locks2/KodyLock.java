package com.dongnaoedu.network.concurrent.period6.locks2;


import com.dongnaoedu.network.humm.多线程.ReenLock.MyReenLock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;


public class    KodyLock implements Lock {

    //private Thread owner = null;

    //锁拥有者
    static    volatile AtomicReference<Thread> owner = new AtomicReference<>();

    //等待队列
    private LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    //记录重入的次数
     volatile AtomicInteger count = new AtomicInteger(0);

    @Override
    public boolean tryLock() {
        //若count！=0，说明锁被占用
        int ct = count.get();
        if (ct!=0){
            //看线程的引用是不是调用该方法的当前线程，是则标识可重入得次数
            if (owner.get() == Thread.currentThread()){
                count.set(ct +1);//单线程无需CAS
                return true;
            }
        }else{
            //如count==0，说明锁未未被占用，CAS操作来抢锁，修改count的值
            if (count.compareAndSet( ct, ct +1)){
                owner.set(Thread.currentThread());
                return true;
            }
        }
        return false;
    }

    @Override
    public void lock() {
        if (!tryLock()){
            //抢锁失败，则将当前线程添加到阻塞队列
            waiters.offer(Thread.currentThread());
            //自旋抢锁  抢不到则park阻塞
            for (;;){
                Thread head = waiters.peek();
                if (head == Thread.currentThread()){
                    if (!tryLock()){
                        LockSupport.park();//抢锁失败，则阻塞
                    }else{
                        waiters.poll();//成功拿到锁则从阻塞队列中移除
                        return;
                    }
                }else{
                    LockSupport.park();
                }
            }
        }
    }

    @Override
    public void unlock() {
        if (tryUnlock()){
            Thread th = waiters.peek();
            if (th !=null){
                LockSupport.unpark(th);
            }
        }
    }


    public boolean tryUnlock(){
        //判断owner是不是自己
        if (owner.get() != Thread.currentThread()){
            throw new IllegalMonitorStateException();
        }else{
            //释放锁，count-1，
            int ct = count.get();
            int nextc = ct -1;
            count.set(nextc);

            //是不是一定将onwer该问null
            if (nextc == 0){
                owner.compareAndSet(Thread.currentThread(),null);
                return true;
            }else{
                //return false;kody写的
                if (count.compareAndSet(nextc,0)){
                    System.out.println("锁释放，count值为0");
                    owner.compareAndSet(Thread.currentThread(),null);
                }
                return true;
            }
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


    public static void main(String[] args) {
        KodyLock lock = new KodyLock();
        new Thread(() -> {
            try {
                lock.lock();
                System.out.println("A开启");
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.unlock();
        },"A").start();
        new Thread(() -> {
            lock.lock();
            System.out.println("B开启");
            lock.unlock();
        },"B").start();
        new Thread(() -> {
            lock.lock();
            System.out.println("C开启");
            lock.unlock();
        },"C").start();

    }
}
