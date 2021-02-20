package com.dongnaoedu.network.concurrent.period6_1.locks5;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

public class KodyReadWriteLock implements ReadWriteLock {

    CommonMask mask = new CommonMask(){
        public boolean tryLock(int acquires) {
            //如果read count ！=0 返回false
            if (readCount.get() !=0)
                return false;

            int wct = writeCount.get();     //拿到 独占锁 当前状态

            if (wct==0){
                if (writeCount.compareAndSet(wct, wct + acquires)){     //通过修改state来抢锁
                    owner =Thread.currentThread();  //  抢到锁后，直接修改owner为当前线程
                    return true;
                }
            }else if (owner == Thread.currentThread()){
                writeCount.set(wct + acquires);     //修改count值
                return true;
            }

            return false;
        }


        public boolean tryUnlock(int releases) {
            //若当前线程没有 持有独占锁
            if(owner!= Thread.currentThread()){
                throw new IllegalMonitorStateException();       //抛IllegalMonitorStateException
            }

            int wc= writeCount.get();
            int nextc = wc - releases;      //计算 独占锁剩余占用
            writeCount.set(nextc);      //不管是否完全释放，都更新count值

            if (nextc==0){  //是否完全释放
                owner = null;   //若完全释放，设置owner为null
                return true;
            }else{
                return false;
            }

        }



        public int tryLockShared(int acquires) {
            for (;;){
                if (writeCount.get()!=0 &&
                        owner != Thread.currentThread())
                    return -1;

                int rct = readCount.get();
                if (readCount.compareAndSet(rct, rct + acquires)){
                    return 1;
                }
            }
        }



        public boolean tryUnLockShared(int releases) {
            for(;;){
                int rc = readCount.get();
                int nextc = rc - releases;
                if (readCount.compareAndSet(rc, nextc)){
                    return nextc==0;
                }
            }
        }
    };

/*    public void lock(){
        mask.lock();
    }

    public boolean tryLock(){
        return mask.tryLock(1);
    }

    public void unLock(){
        mask.unlock();
    }

    public void lockShared(){
        mask.lockShared();
    }

    public boolean tryLockSahred(){
        boolean result = mask.tryLockShared(1)==1;
        return result;
    }

    public void unLockShared(){
        mask.unLockShared();
    }*/


    @Override
    public Lock readLock() {
        return new Lock() {
            @Override
            public void lock() {
                mask.lockShared();
            }

            @Override
            public void lockInterruptibly() throws InterruptedException {

            }

            @Override
            public boolean tryLock() {
                return mask.tryLockShared(1)==1;
            }

            @Override
            public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
                return false;
            }

            @Override
            public void unlock() {
                mask.unLockShared();
            }

            @Override
            public Condition newCondition() {
                return null;
            }
        };
    }

    @Override
    public Lock writeLock() {
        return new Lock() {
            @Override
            public void lock() {
                mask.lock();
            }

            @Override
            public void lockInterruptibly() throws InterruptedException {

            }

            @Override
            public boolean tryLock() {
                return mask.tryLock(1);
            }

            @Override
            public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
                return false;
            }

            @Override
            public void unlock() {
                mask.unlock();
            }

            @Override
            public Condition newCondition() {
                return null;
            }
        };
    }
}
