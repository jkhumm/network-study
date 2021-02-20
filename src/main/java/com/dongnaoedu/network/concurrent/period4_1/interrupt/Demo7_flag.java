package com.dongnaoedu.network.concurrent.period4_1.interrupt;

public class Demo7_flag {

    public static void main(String[] args) throws InterruptedException {
        Thread testThread = new Thread(){
            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()){
               /*     try {
                        System.out.println("runing...");
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //isinterrupted状态被擦除
                        Thread.currentThread().interrupt();
                    }*/
                    System.out.println("fasdfads");
                }
            }
        };
        testThread.start();


        Thread.sleep(1000);
        testThread.interrupt();

    }



}
