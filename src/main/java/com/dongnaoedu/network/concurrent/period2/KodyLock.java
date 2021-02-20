package com.dongnaoedu.network.concurrent.period2;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

//这个KodyLock的实现目前还并不能使用，但是整个逻辑没问题，
//大家从这个KodyLock实现来体会“线程通信”的应用

public class KodyLock implements Lock {

    //当前锁拥有者，若onwer为null，说明没有线程占用锁
    private Thread owner = null;

    //锁占用是，线程被挂起，挂起的线程的引用被放到waiters队列
    private BlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();


    @Override
    public boolean tryLock() {  //尝试获取锁
        //若锁未占用
        if (owner == null){
            owner = Thread.currentThread();     //当前线程获得锁
            return true;        //获得锁，返回true
        }else{
            return false;       //获取锁失败，返回false
        }
    }

    @Override
    public void lock() {
/*        if (!tryLock()){
            LockSupport.park();
            lock();     //递归在底层开发中尽量少用，方法调用层次太多，虚拟机栈可能过深，报 StackOverflowError
        }*/

        while(!tryLock()){      //尝试拿锁，如果失败，
            Thread curTh = Thread.currentThread();      //获取当前线程引用
            waiters.offer(curTh);       //将当前线程引用放入等待队列

            //wait;   //  syrnchronized     //wait需要在synchronized中使用，用已有的锁来实现我们自己的锁，不地道
            //suspend   //弃用，太容易死锁
            LockSupport.park();     // 用线程通信方式park，将当前线程挂起（进入WAITING状态），
            //从park状态被唤醒，继续循环拿锁
            //思考？这里如何处理，可以避免潜在的伪唤醒问题
        }
    }

    @Override
    public void unlock() {  //释放锁
        if (owner == Thread.currentThread()){       //若确实是我占有了锁
            owner = null;                           //将onwer置为null，释放锁
            Thread th = waiters.poll();     //取出队列头部的元素，并移除该元素
            LockSupport.unpark(th);           //唤醒队列头部的元素
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
