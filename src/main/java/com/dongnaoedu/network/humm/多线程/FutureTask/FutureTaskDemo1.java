package com.dongnaoedu.network.humm.多线程.FutureTask;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

/**
 * @author heian
 * @create 2020-02-22-3:11 下午
 * @description
 */
public class FutureTaskDemo1 {

    public static void main(String[] args) throws InterruptedException {
        Callable<String> call = new Callable<String>() {
            @Override
            public String call() throws Exception {
                String name = Thread.currentThread().getName();
                System.out.println(name);
                return name;
            }
        };
        //只会运行一次
        FutureTask<String> futureTask = new FutureTask(call);
        new Thread(futureTask).start();//不会执行
        new Thread(futureTask).start();//不会执行

    }



}
