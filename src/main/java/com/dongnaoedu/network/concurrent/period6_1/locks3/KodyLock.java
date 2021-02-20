package com.dongnaoedu.network.concurrent.period6_1.locks3;



public class KodyLock {

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


}
