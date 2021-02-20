package com.dongnaoedu.network.concurrent.period4_1;

import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

public class Demo7_FutureTask<T> implements Runnable  {

    private volatile boolean isEnding = false;

    private T result;

    private Callable<T> callable;

    LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue(100);

    public Demo7_FutureTask(Callable<T> task){
        this.callable = task;
    }



    @Override
    public void run() {
        try {
            result = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            isEnding = true;
        }

        //方法执行完，唤醒所有线程
        while (true) {
            Thread waiter = waiters.poll();
            if (waiter == null){
                break;
            }
            LockSupport.unpark(waiter);
        }
    }

    //调用方法，阻塞等待结果
    public T get(){
        //如果线程执行完毕，就返回结果
        if (!isEnding){
            waiters.offer(Thread.currentThread());
        }

        while (!isEnding){
            LockSupport.park();
        }

        return result;
    }
}