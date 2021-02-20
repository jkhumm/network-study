package com.dongnaoedu.network.humm.多线程.ReenLock.AQS;


import java.util.concurrent.locks.LockSupport;

/**
 * @author heian
 * @create 2020-03-07-4:18 下午
 * @description
 */
public class MyReadWriteLock {

    private  boolean isFair;
    private Sync sync ;

    public MyReadWriteLock(boolean isFair) {
        this.isFair = isFair;
        this.sync = new Sync();
    }
    public MyReadWriteLock() {
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

        /**
         * acquireShared()
         * 尝试获取读锁
         * 只修改count 因为reference是被多个线程引用
         * 这里返回int 是为了模拟jdk源码
         */
        @Override
        public int tryLockShared(int acquires){
            for (;;){
                if (writeCount.get() != 0 ){//&& Thread.currentThread() != reference.get()
                    return -1;
                }else {
                    int readNum = readCount.get();
                    //多个读线程 CAS操作改变，可能会失败所以需要自旋for
                    if (readCount.compareAndSet(readNum,readNum + acquires)){
                        return 1;
                    }
                }
            }
        }

        /**
         * tryReleaseShared
         * 尝试解开读锁,就直接修改readCount值即可
         * 解锁不用判断写锁占用情况：读锁重入多次，解锁1次，则count就！=0，只有当读锁的count=0，才能表明读锁释放锁成功
         */
        @Override
        public boolean tryUnlockShared(int releases){
            //这里无需去唤醒读锁，因为这里你解锁也只能唤醒一个，而是应该放在lockShared方法里，全部唤醒处在头部的读锁
            for (;;){
                int readNum = readCount.get();
                int readNum2 = readNum - releases;
                if (readCount.compareAndSet(readNum,readNum2)){
                    return readNum2 == 0;
                }
            }
        }

    }

    public void lock(){
        sync.lock();
    }

    public boolean unlock(){
        return sync.unlock();
    }

    public void lockShared(){
        sync.lockShared();
    }

    public boolean unlockShared() {
        return sync.unlockShared();
    }

    public static void main(String[] args) {
        MyReadWriteLock lock = new MyReadWriteLock();

        new Thread(() -> {
            lock.lock();
            System.out.println("写线程A开始");
            LockSupport.parkNanos(1000000000*3L);
            System.out.println("写线程A结束");
            lock.unlock();
        }).start();

        new Thread(() -> {
            lock.lock();
            System.out.println("写线程B开始");
            LockSupport.parkNanos(1000000000*3L);
            System.out.println("写线程B结束");
            lock.unlock();
        }).start();

        new Thread(() -> {
            lock.lockShared();
            System.out.println("读线程C开始");
            LockSupport.parkNanos(1000000000*3L);
            System.out.println("读线程C结束");
            lock.unlockShared();
        }).start();

        new Thread(() -> {
            lock.lockShared();
            System.out.println("读线程D开始");
            LockSupport.parkNanos(1000000000*3L);
            System.out.println("读线程D结束");
            lock.unlockShared();
        }).start();
    }


}






