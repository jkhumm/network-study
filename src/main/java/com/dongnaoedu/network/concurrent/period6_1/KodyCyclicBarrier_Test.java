package com.dongnaoedu.network.concurrent.period6_1;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class KodyCyclicBarrier_Test {
    public static void main(String args[]) throws BrokenBarrierException, InterruptedException {
        KodyCyclicBarrier barrier = new KodyCyclicBarrier(4);
       // CyclicBarrier barrier = new CyclicBarrier(4);
        for (int i=0; i<10; i++){
            Thread th = new Thread(){
                @Override
                public void run() {
                    try {
                        barrier.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("run...");
                }
            };

            th.start();
            Thread.sleep(1000);
        }

    }



}
