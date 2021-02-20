package com.dongnaoedu.network.humm.多线程.FutureTask;

import java.lang.ref.SoftReference;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.LockSupport;

/**
 * @author heian
 * @create 2020-02-23-3:55 下午
 * @description 该对象可能会被提交到多个线程执行
 * 1、任务来了，就执行call方法（线程运行了此任务）
 * 2、当你调get方法，此时该线程还没执行那个完成，所以需要阻塞
 * 3、任务执行完成，你就唤醒一个线程
 * 但是此处并没有唤醒指定的线程,所以拿到的结果也只能是某个执行好了的线程的一个结果，因为大多数方法都不需要指定顺序
 */
public class MyFutureTask<T> implements Runnable {

    private Callable<T> callable;
    private volatile T result;
    private volatile boolean isFinshed =false;
    //队列：用来标示那个线程需要等待，哪些需要唤醒
    private LinkedBlockingQueue<Thread> queue = new LinkedBlockingQueue<>(100);

    public MyFutureTask(Callable<T> callable){
        this.callable = callable;
    }


    public T get(){
        //因为此方法是阻塞方法，必须等结果执行完成后，才唤醒该线程
        if (!isFinshed){
            //假设当某个线程调用，它没执行完成，先将该线程放入队列，在挂起该线程
            queue.offer(Thread.currentThread());
        }
        //防止伪唤醒问题，所以用while
        while (!isFinshed){
            LockSupport.park();
        }
        return result;
    };

    @Override
    public void run() {
        try {
            result = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            isFinshed = true;
        }
        //如果任务完成了，则从队列中唤醒并移除
        while (true){
            Thread currentThread = queue.poll();
            if (currentThread == null){
                break;
            }
            LockSupport.unpark(currentThread);
        }
    }

    public static void main(String[] args) {
        Callable<String> call = new Callable<String>() {
            @Override
            public String call() throws Exception {
                System.out.println(Thread.currentThread().getName());
                return Thread.currentThread().getName();
            }
        };
        MyFutureTask<String> futureTask = new MyFutureTask(call);
        //new Thread(futureTask,"A").start();
        //new Thread(futureTask,"B").start();
        //String resu = futureTask.get();
        //System.out.println(resu);

        FutureTask<String> f = new FutureTask<>(call);
        new Thread(f).start();
        new Thread(f).start();







    }



}
