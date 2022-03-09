package com.dongnaoedu.network.humm.多线程.ReenLock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author heian
 * @create 2020-03-05-10:04 下午
 * @备注 不是公平锁，在调用lock方法中第二个tryLock方法时候，没加判断，可能存在锁释放时唤醒了队列中的某个线程，
 * 外来线程调用又tryLock()方法此时也正好来抢锁，那此时存在非公平现象
 */
public class MyReentrantLock {

    //想获取锁的线程
    private LinkedBlockingQueue<Thread> queue = new LinkedBlockingQueue<>();
    //被引用的线程
    private AtomicReference<Thread> reference = new AtomicReference<>();
    //计算重入得次数
    private AtomicInteger count = new AtomicInteger(0);

    /**
     * 尝试获取锁（不是真的去拿锁，所以不用加入到阻塞队列）
     * 修改count 和 reference
     */
    public boolean tryLock(){
        if (count.get() != 0){
            //锁被占用（可能是自己）
            if (Thread.currentThread() == reference.get()){
                count.set(count.get()+1);//单线程  无需CAS
            }
        }else {
            //为了实现公平锁，需要判断进来的线程是不是队列头部线程，不是则直接返回false(不让外来线程可乘之机)
            if (queue != null){
                if (queue.peek() == Thread.currentThread()
                        && count.compareAndSet(count.get(),count.get()+1)){
                    reference.set(Thread.currentThread());
                    return true;
                }
            }else{
                if (count.compareAndSet(count.get(),count.get()+1)){
                    reference.set(Thread.currentThread());
                    return true;
                }
            }


        }
        return false;
    }

    /**
     * 抢锁（存在非公平现象）
     * 修改 queue
     */
    public void lock(){
        //锁被占用，则CAS自旋不断地去抢锁
        if (!tryLock()){
            queue.offer(Thread.currentThread());
            //lock 是不死不休所以得用for循环，既然CAS拿不到则由轻量级锁转为重量级锁（挂起阻塞）再一次去拿锁
            for (;;){
                Thread headThread = queue.peek();
                //进入这段代码表明你可能是刚进来的也可能是被唤醒的
                if (headThread == Thread.currentThread()){
                    //都会去判断是不是头部，是头部则表示自己是被唤醒的或者刚进来的
                    //被唤醒的需要再一次抢锁（即使是被唤醒的此时也有可能被外来线程抢锁），不是则乖乖阻塞着
                    if (tryLock()){
                        queue.poll();
                        break;
                    }else {
                        //是头部线程元素（一开始进来的线程，或者唤醒不是队列头部的线程），但是在此在队列并挂起
                        LockSupport.park();
                    }
                }else {
                    //不是头部线程，在队列并挂起
                    LockSupport.park();
                }
            }

        }
    }

    /**
     * 释放锁
     * 修改 queue
     */
    public void unlock(){
        if (tryUnlock()){
            Thread peek = queue.peek();
            //存在队列为空可能，比如就一个抢锁的不会去加入到阻塞队列
            if (peek != null){
                LockSupport.unpark(peek);
            }
        }
    }

    /**
     * 尝试去解锁
     * 修改count 和 reference
     */
    public boolean tryUnlock(){
        if (reference.get() != Thread.currentThread()){
            throw new IllegalMonitorStateException("未能获取到锁，无法释放锁");
        }else {
            //只有是拿到锁的线程才有解锁的资格，所以此处是单线程
            int value = count.get()- 1;
            count.set(value);
            //当你lock多次，但是unlock一次，此时是不会释放锁，只是不阻塞罢了
            if (value == 0){
                reference.set(null);
                return true;
            }else {
                return false;
            }
        }
    }


    public static void main(String[] args) {
        //MyReadWriteLock lock = new MyReadWriteLock();
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        new Thread(() -> {
            lock.readLock().lock();
            System.out.println("读锁开始工作");
            LockSupport.parkNanos(1000000000*3L);
            System.out.println("读锁结束工作");
            lock.readLock().unlock();
        }).start();
        new Thread(() -> {
            lock.readLock().lock();
            System.out.println("读锁开始工作");
            LockSupport.parkNanos(1000000000*3L);
            System.out.println("读锁结束工作");
            lock.readLock().unlock();
        }).start();

        new Thread(() -> {
            lock.writeLock().lock();
            System.out.println("写锁开始工作");
            LockSupport.parkNanos(1000000000*3L);
            System.out.println("写锁结束工作");
            lock.writeLock().unlock();
        }).start();

    }

}
