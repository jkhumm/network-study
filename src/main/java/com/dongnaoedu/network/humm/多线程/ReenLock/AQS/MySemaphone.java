package com.dongnaoedu.network.humm.多线程.ReenLock.AQS;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.LockSupport;
import java.util.stream.IntStream;

/**
 * @author heian
 * @create 2020-03-07-9:08 下午
 * @description 信号量 进来就拿锁，拿到了就+1，释放了就-1，共享锁的线程只允许最大连接permit个线程
 */
public class MySemaphone {

    private Sync sync;
    private int permitCount;
    public MySemaphone(int count){
        if (count<0){
            throw new IllegalArgumentException("参数传入有误");
        }
        this.sync= new Sync();
        this.permitCount = count;
    }

    public void acquire() {
        sync.acquireShared(1);
    }
    public void release() {
        sync.releaseShared(1);
    }

    class Sync extends AbstractQueuedSynchronizer{

        @Override
        protected int tryAcquireShared(int arg) {
            for (;;){
                int currentReadCount = getState() + arg;
                if (currentReadCount<=permitCount){
                    if (compareAndSetState(getState(),currentReadCount)){
                        return 1;
                    }
                }else {
                    return -1;
                }
            }
        }

        @Override
        protected boolean tryReleaseShared(int arg) {
            //System.out.println("打印队列长度" + getQueueLength());
            for (;;){
                int remainNum = getState() - arg;
                return compareAndSetState(getState(),remainNum);
            }
        }
    }




    public static void main(String[] args)  {
        MySemaphone semaphore = new MySemaphone(3);
        IntStream.range(0,5).forEach(value -> {
            new Thread(() -> {
                semaphore.acquire();
                System.out.println(Thread.currentThread().getName() + "    获得许可");
                LockSupport.parkNanos(1000000000*2L);
                System.out.println(Thread.currentThread().getName() + "释放许可");
                semaphore.release();
            }).start();
        });
    }


}
