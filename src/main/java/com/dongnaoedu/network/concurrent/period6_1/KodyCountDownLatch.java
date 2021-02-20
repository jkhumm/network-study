package com.dongnaoedu.network.concurrent.period6_1;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

//CountDownLatch的本质就是一个共享锁
public class KodyCountDownLatch {

    //CountDownLatch也可以看做一个共享锁
    //这个共享锁被获取了n次，n为倒计数初始值
    //只有在被释放后，才能获取锁

    Sync sync;
    KodyCountDownLatch(int count){
        this.sync = new Sync(count);
    }


    public void countDown(){
        this.sync.releaseShared(1);
    }

    public void await(){
        this.sync.acquireShared(1);
    }



    class Sync extends AbstractQueuedSynchronizer{
        public Sync(int count){
            setState(count);
        }

        //相当于await方法，只有count=0 的时候，才会成功
        @Override
        protected int tryAcquireShared(int arg) {
            return getState() ==0 ? 1 : -1;
        }


        //相当于countDown
        @Override
        protected boolean tryReleaseShared(int arg) {
            for (;;){
                int c = getState();
                if (c==0){
                    return false;
                }
                int nextc = c -1;
                if (compareAndSetState(c ,nextc)){
                    return nextc == 0;
                }
            }
        }
    }


}


