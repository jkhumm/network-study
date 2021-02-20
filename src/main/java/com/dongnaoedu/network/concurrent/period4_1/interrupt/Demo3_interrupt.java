package com.dongnaoedu.network.concurrent.period4_1.interrupt;


public class Demo3_interrupt {
    public static void main(String[] args) throws InterruptedException {
        Thread testThread = new Thread(){
            @Override
            public void run() {
                while (true){
                    try {
                        System.out.println("runing...");
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        //当线程处于 Waiting、TimedWaiting状态，
                        //执行interrupted方法后，会从Waiting、TimedWaiting中退出
                        //并且isInterrupted=true的信号被擦除
                        System.out.println(Thread.currentThread().getState());
                        System.out.println("isInterrupted:" + Thread.currentThread().isInterrupted());
                    }

/*                    LockSupport.park();     //当调用 interrupted方法后，这个park就失效了
                    System.out.println("调用interrupt方法后，park()方法失效...");*/
                }
            }
        };
        testThread.start();

        Thread.sleep(1000);
        testThread.interrupt();


    }
}
