package com.dongnaoedu.network.concurrent.period4_1.interrupt;

public class Demo2_interrupt {

    public static void main(String[] args) throws InterruptedException {
        //开启一个线程
        Thread testThread = new Thread(){
            @Override
            public void run() {
                while(true){
                    System.out.println("isInterrupted:" + Thread.currentThread().isInterrupted());
                    System.out.println("state:" + Thread.currentThread().getState());
                }
            }
        };

        testThread.start();
        Thread.sleep(2000);

        //调用interrupt方法，不会改变线程状态
        //并不会让线程退出，只是做了一个interrupt标记
        testThread.interrupt();


    }

}
