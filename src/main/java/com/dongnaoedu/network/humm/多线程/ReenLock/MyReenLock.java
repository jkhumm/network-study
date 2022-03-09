package com.dongnaoedu.network.humm.多线程.ReenLock;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author heian
 * @create 2020-02-24-8:54 上午
 * @description 非公平锁
 */
public class MyReenLock {
    //想获取锁的线程
    private LinkedBlockingQueue<Thread> queue = new LinkedBlockingQueue<>(10);
    //被引用的线程
    private AtomicReference<Thread> reference = new AtomicReference<>();
    //计算重入得次数
    private AtomicInteger count = new AtomicInteger(0);

    /**
     * 1、尝试获取锁,不是真的想去拿锁，所以不用加入到阻塞队列，并且此时锁是处于占据状态
     * 2、不是公平锁，在调用lock中tryLock方法时候，没加判断，
     *    存在当锁正好没被占用，唤醒了队列中的某个线程，外来线程此时也正好来抢锁。存在非公平现象
     */
    public boolean tryLock(){
        if (count.get() != 0){
            //锁被占用，判断是不是自己占着,是自己则更新重入值，无需更新引用
            if (reference.get() == Thread.currentThread()){
                if (count.compareAndSet(count.get(),count.get() + 1))
                    return true;
            }
        }else {
            //锁没被占用
            count.compareAndSet(count.get(),1);//count
            reference.compareAndSet(null,Thread.currentThread());
            return true;
        }
        return false;
    }

    /**
     * 上锁  被占用则阻塞
     */
    public void lock(){
        //为了实现公平锁，根据先进先出原则
        if (!tryLock()){
            System.out.println(Thread.currentThread().getName() + "----CAS抢锁失败，添加到队列");
            queue.offer(Thread.currentThread());
            //当拿到锁的线程释放锁，则队列头部线程应当出来占锁，并且应该实现自动复苏
            //当线程被唤醒后，为了实现公平性，要从头再来判断下，
            //lock 是不死不休，既然CAS拿不到则由轻量级锁转为重量级锁，再次去拿锁
            for (;;){
                Thread headThread = queue.peek();
                System.out.println("当前执行线程" + Thread.currentThread() + "转为重量级锁，头部元素" + headThread
                        +"，是否是当前线程"+(headThread == Thread.currentThread())+"，count:" + count.get());
                if (headThread == Thread.currentThread()){
                    if (tryLock()){
                        Thread poll = queue.poll();
                        System.out.println(Thread.currentThread() + "拿到锁，并且从队列中移除"+poll);
                        break;
                    }else {
                        System.out.println(Thread.currentThread() + "陷入阻塞，头部元素");
                        LockSupport.park();//阻塞
                        System.out.println(Thread.currentThread() + "被唤醒，头部元素");
                    }
                }else {
                    System.out.println(Thread.currentThread() + "陷入阻塞，非头部元素");
                    LockSupport.park();//阻塞
                    System.out.println(Thread.currentThread() + "被唤醒，非头部元素");
                }
            }
        }
    }

    /**
     * 解锁
     */
    public void unlock(){
        if (tryUnlock()){
            //可以解锁，其实就是唤醒park的线程
            Thread peek = queue.peek();
            if (peek != null){
                LockSupport.unpark(peek);
            }
        }
    }

    /**
     * 尝试解锁
     */
    public boolean tryUnlock(){
        //厕所被别人占用，你试图去开人家门，是不允许的
        if (reference.get() != Thread.currentThread()){
            throw new IllegalMonitorStateException();
        }else {
            //当你lock多次，但是unlock一次，此时是不会释放锁，只是不阻塞罢了
            count.set(count.get()-1);
            if (count.get() == 0){
                reference.compareAndSet(Thread.currentThread(),null);
                return true;
            }else {
                return false;
            }
        }
    }

    public static void main(String[] args){
        ReentrantLock reentrantLock = new ReentrantLock();
        new Thread(() -> {
            reentrantLock.tryLock();
            LockSupport.parkNanos(1000*1000*1000*10L);
            reentrantLock.unlock();
        }).start();
        LockSupport.parkNanos(1000*1000*100*1L);
        new Thread(() -> {
            reentrantLock.lock();
            System.out.println("111");
            reentrantLock.unlock();
        }).start();
    }


}
