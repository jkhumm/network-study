package com.dongnaoedu.network.concurrent.period8.queue;

import java.util.concurrent.SynchronousQueue;

/*

1、put时会阻塞，直到被get
2、take会阻塞，直到取到元素

3、offer的元素可能会丢失
4、poll取不到元素，就返回null,如果正好有take被阻塞，可以取到
5、peek 永远只能取到null，不能让take结束阻塞

 */

public class Demo2_SyncQueueTest {
    public static void main(String args[]) throws InterruptedException {

        SynchronousQueue<String> syncQueue = new SynchronousQueue<>();


/*        new Thread(){
            @Override
            public void run() {
                try {
                    System.out.println("begain to put...");
                    syncQueue.put("fasdfads");
                    System.out.println("put done...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
        Thread.sleep(3000);*/

/*
        Object str = syncQueue.take();
        System.out.println(str);*/

/*        Object str = syncQueue.poll();
        System.out.println(str);*/

/*        Object str = syncQueue.peek();       //peak
        System.out.println(str);*/


        new Thread(){
            @Override
            public void run() {
                try {
                    System.out.println("begain to take...");
                    System.out.println(syncQueue.take());
                    System.out.println("take done...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }.start();
        Thread.sleep(3000);


        //syncQueue.offer("offer value");

        syncQueue.put("put value");




/*
        new Thread(){
            @Override
            public void run() {
                try {
                    System.out.println("begin to put...");
                    syncQueue.put("put ele...");
                    System.out.println("put done...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.start();*/


/*        Thread.sleep(3000L);
        String ele = syncQueue.peek();
        System.out.println("peek " + ele);*/
/*
        Thread.sleep(3000L);
        String ele1 = syncQueue.poll();
        System.out.println("poll:" + ele1);*/

    }
}
