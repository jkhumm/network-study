package com.dongnaoedu.network.concurrent.period6_1;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class KodySemaphore {

    private int count;
    private Sync sync;

    public KodySemaphore(int count){
        this.count = count;
        this.sync = new Sync();
    }

    public void acquire(){
        sync.acquireShared(1);
    }

    public void release(){
        sync.releaseShared(1);
    }



    //信号量，就是一个共享锁，
    //这个共享锁同时只能被n个人获取，n为信号量数量

    class Sync extends AbstractQueuedSynchronizer{

        @Override
        protected int tryAcquireShared(int arg) {
            for (;;){
                int state = getState();
                int nextSatte = state + arg;
                if (nextSatte <= count){
                    compareAndSetState(state, nextSatte);
                    return 1;
                }else{
                    return -1;
                }
            }
        }


        @Override
        protected boolean tryReleaseShared(int arg) {
            for (;;){
                int state = getState();
                if (compareAndSetState(state, state-arg)){
                    return true;
                }else{
                    return false;
                }
            }

        }
    }



}
