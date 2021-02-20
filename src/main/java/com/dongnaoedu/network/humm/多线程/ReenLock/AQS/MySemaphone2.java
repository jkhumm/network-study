package com.dongnaoedu.network.humm.多线程.ReenLock.AQS;

import java.util.concurrent.locks.LockSupport;
import java.util.stream.IntStream;

/**
 * @author heian
 * @create 2020-03-07-9:08 下午
 * @description 信号量 进来就拿锁，拿到了就+1，释放了就-1，共享锁的线程只允许最大连接permit个线程
 */
public class MySemaphone2 {

    private Sync sync;
    private int permitCount;

    public MySemaphone2(int count){
        if (count<0){
            throw new IllegalArgumentException("参数传入有误");
        }
        this.sync= new Sync();
        this.permitCount = count;
    }

    public void acquire() {
        this.sync.lock();
    }
    public void release() {
        this.sync.unlock();
    }

    class Sync extends MyAQS{

        @Override
        public int tryLockShared(int arg){
            for (;;){
                int currentReadCount = readCount.get()+ arg;
                if (currentReadCount<=permitCount){
                    if (readCount.compareAndSet(readCount.get(),currentReadCount)){
                        return 1;
                    }
                }else {
                    return -1;
                }
            }
        }

        @Override
        public boolean tryUnlockShared(int arg) {
            //System.out.println("打印队列长度" + getQueueLength());
            for (;;){
                int remainNum = readCount.get() - arg;
                return readCount.compareAndSet(readCount.get(),remainNum);
            }
        }
    }




    public static void main(String[] args)  {
        MySemaphone2 mySemaphone2 = new MySemaphone2(3);
        IntStream.range(0,5).forEach(value -> {
            new Thread(() -> {
                mySemaphone2.acquire();
                System.out.println(Thread.currentThread().getName() + "    获得许可");
                LockSupport.parkNanos(1000000000*2L);
                System.out.println(Thread.currentThread().getName() + "释放许可");
                mySemaphone2.release();
            }).start();
        });
    }


}
