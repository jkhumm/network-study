package com.dongnaoedu.network.humm.多线程;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author heian
 * @date 2023/10/30 23:00
 * @description 验证达到核心线程数，看是放到队列还是继续创建线程
 */
public class ThreadPoolTest {


    static ThreadPoolExecutor executor = new ThreadPoolExecutor(3, 5, 60, TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(10));


    public static void main(String[] args) throws InterruptedException {

        for (int i = 0; i < 3; i++) {
            executor.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        TimeUnit.SECONDS.sleep(1);
        System.out.println("size1:" +  executor.getQueue().remainingCapacity());
        System.out.println("active1:" + executor.getActiveCount());

        TimeUnit.SECONDS.sleep(1);
        executor.execute(()->{
            try {
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        TimeUnit.SECONDS.sleep(1);
        System.out.println("size2:" +  executor.getQueue().remainingCapacity());
        System.out.println("active2:" + executor.getActiveCount());
    }


}
