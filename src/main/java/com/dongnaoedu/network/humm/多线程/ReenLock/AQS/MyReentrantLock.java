package com.dongnaoedu.network.humm.多线程.ReenLock.AQS;


import java.util.concurrent.locks.LockSupport;

/**
 * @author heian
 * @create 2020-03-07-4:18 下午
 * @description 实现自己的AQS
 * 实现公平锁与非公平锁
 */
public class MyReentrantLock {

    private boolean isFair;
    private Sync sync;

    public MyReentrantLock(boolean isFair) {
        this.isFair = isFair;
        this.sync = new Sync();
    }
    public MyReentrantLock() {
        this.sync = new Sync();
    }


    //采取源码方式  内部类继承
    class Sync extends MyAQS{

        @Override
        public boolean tryLock(int acquires){
            return isFair == true ? tryFairLock(acquires):tryNonFairLock(acquires);
        };
        //非公平锁实现方式
        public boolean tryNonFairLock(int acquires){
            if (readCount.get() != 0){
                return false;
            }
            if (writeCount.get() != 0){
                //锁被占用（可能是自己）
                if (Thread.currentThread() == reference.get()){
                    writeCount.set(writeCount.get()+acquires);//单线程  无需CAS
                }
            }else {
                //非公平的实现
                if (writeCount.compareAndSet(writeCount.get(),writeCount.get()+1)){
                    reference.set(Thread.currentThread());
                    return true;
                }
            }
            return false;
        }
        //公平锁实现方式
        public boolean tryFairLock(int acquires)  {
            if (readCount.get() != 0){
                return false;
            }
            if (writeCount.get() != 0){
                //锁被占用（可能是自己）
                if (Thread.currentThread() == reference.get()){
                    writeCount.set(writeCount.get()+acquires);//单线程  无需CAS
                }
            }else {
                //为了实现公平锁，需要判断进来的线程是不是队列头部线程，不是则直接返回false(不让外来线程可乘之机)
                if (queue != null){
                    if (queue.peek().thread == Thread.currentThread()
                            && writeCount.compareAndSet(writeCount.get(),writeCount.get()+acquires)){
                        reference.set(Thread.currentThread());
                        return true;
                    }
                }else{
                    if (writeCount.compareAndSet(writeCount.get(),writeCount.get()+acquires)){
                        reference.set(Thread.currentThread());
                        return true;
                    }
                }

            }
            return false;
        }

        /**
         * 尝试去解锁
         * 解锁不用判断读锁占用情况：，修改count 和 reference
         */
        @Override
        public boolean tryUnlock(int releases){
            if (reference.get() != Thread.currentThread()){
                throw new IllegalMonitorStateException("未能获取到锁，无法释放锁");
            }else {
                //只有是拿到锁的线程才有解锁的资格，所以此处是单线程
                int value = writeCount.get()- releases;
                writeCount.set(value);
                //当你lock多次，但是unlock一次，此时是不会释放锁，只是不阻塞罢了
                if (value == 0){
                    reference.set(null);
                    return true;
                }else {
                    return false;
                }
            }
        }

    }


    public void lock(){
        sync.lock();
    }
    public boolean tryLock(){
        return sync.tryLock(1);
    }
    public void unlock(){
        sync.unlock();
    }


    public static void main(String[] args) {
        MyReentrantLock lock = new MyReentrantLock();

        new Thread(() -> {
            lock.lock();
            System.out.println("A开始");
            LockSupport.parkNanos(1000000000*3L);
            System.out.println("A结束");
            lock.unlock();
        }).start();

        new Thread(() -> {
            lock.lock();
            System.out.println("B开始");
            LockSupport.parkNanos(1000000000*1L);
            System.out.println("B结束");
            lock.unlock();
        }).start();

        new Thread(() -> {
            lock.lock();
            System.out.println("C开始");
            LockSupport.parkNanos(1000000000*1L);
            System.out.println("C结束");
            lock.unlock();
        }).start();

    }





}
