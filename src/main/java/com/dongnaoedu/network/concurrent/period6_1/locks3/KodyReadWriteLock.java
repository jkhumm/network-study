package com.dongnaoedu.network.concurrent.period6_1.locks3;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;


public class KodyReadWriteLock {

    CommonMask mask = new CommonMask();

    public void lock(){
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
    }



}
