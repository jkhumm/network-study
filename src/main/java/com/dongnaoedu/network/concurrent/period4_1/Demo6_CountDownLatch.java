package com.dongnaoedu.network.concurrent.period4_1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

//Count Down Latch 倒计时开关
//CountDownLatch的基本使用
public class Demo6_CountDownLatch {

    static AtomicLong num = new AtomicLong(0);

    public static void main(String args[]) throws InterruptedException {

        CountDownLatch ctl = new CountDownLatch(11);

        for (int i=0; i< 10; i++){
            new Thread(){
                @Override
                public void run() {
                    for (int j=0; j< 10000000; j++){
                        num.getAndIncrement();
                    }
                    ctl.countDown();
                }
            }.start();
        }

        ctl.await();     //设置开关，设置门栓
        System.out.println(num.get());
    }





}
