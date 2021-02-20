package com.dongnaoedu.network.concurrent.period6_1;

public class KodySemaphore_Test {

    public static void main(String args[]) throws InterruptedException {
        KodySemaphore sp = new KodySemaphore(3);

        for (int i=0;i< 10; i++){
            new Thread(){
                @Override
                public void run() {
                    sp.acquire();
                    System.out.println(" here i am...");
                    try {
                        Thread.sleep(3000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    sp.release();
                }
            }.start();
            Thread.sleep(10L);
        }

    }



}
