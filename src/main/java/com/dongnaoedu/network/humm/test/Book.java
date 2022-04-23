package com.dongnaoedu.network.humm.test;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author heian
 * @create 2020-03-09-10:52 上午
 * @description
 */
public class Book {


    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        executorService.execute(() -> {
            System.out.println("线程1" + Thread.currentThread().getName());
        });
        executorService.execute(() -> {
            System.out.println("线程2" + Thread.currentThread().getName());
        });
        executorService.execute(() -> {
            System.out.println("线程3" + Thread.currentThread().getName());
        });
        // 会继续创建线程
        TimeUnit.SECONDS.sleep(2);
        executorService.execute(() -> {
            System.out.println("线程4" + Thread.currentThread().getName());
        });
    }


}
