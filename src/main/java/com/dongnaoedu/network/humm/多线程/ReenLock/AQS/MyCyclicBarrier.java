package com.dongnaoedu.network.humm.多线程.ReenLock.AQS;

import java.util.concurrent.locks.LockSupport;
import java.util.stream.IntStream;

/**
 * @author heian
 * @create 2020-03-08-2:34 下午
 * @description
 */
public class MyCyclicBarrier {

    private int initCount = 0;
    private int permitCount;
    private Object generation = new Object();

    public MyCyclicBarrier(int permitCount){
        this.permitCount = permitCount;
    }

    public void await() throws InterruptedException {
        synchronized (this){
            initCount++;
            Object currentG = generation;
            if (initCount == permitCount){
                //进入下一次计数
                nextGeneration();
            }else {
                for (;;){
                    this.wait();//释放锁
                    //被更改了，则直接跳出阻塞
                    if (currentG != generation){
                        break;
                    }
                }
            }
        }
    }

    public void nextGeneration(){
        synchronized (this){
            System.out.println("--");
            initCount =0;
            generation = new Object();
            notifyAll();
        }
    }

    public static void main(String[] args) {
        MyCyclicBarrier cyclicBarrier = new MyCyclicBarrier(3);
        IntStream.range(0,7).forEach(value -> {
            new Thread(() -> {
                try {
                    cyclicBarrier.await ();
                    System.out.println (Thread.currentThread ().getName () + "一起跳出栅栏");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        });

        System.out.println(3%4);
    }

}
