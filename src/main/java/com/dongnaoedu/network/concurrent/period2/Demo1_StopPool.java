package com.dongnaoedu.network.concurrent.period2;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

public class Demo1_StopPool {

    static ThreadPoolExecutor pool = new ThreadPoolExecutor(
            5,
            10,
            5,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<Runnable>(),
            new RejectedExecutionHandler() {
                public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                    System.err.println("有任务被拒绝执行了");
                }
            }
    );


    public static void main(String args[]) throws Exception {
        new Thread(new Runnable() {
            public void run() {
                //每隔1s submit一个任务
                for (int i = 0; i < 9; i++) {
                    final int n = i;
                    LockSupport.parkNanos(1000 * 1000 * 1000 * 1L); //挂起1s
                    System.out.println("尝试提交一个任务。。。");

                    pool.submit(new Runnable() {
                        public void run() {
                            try {
                                System.out.println("开始执行：" + n);
                                Thread.sleep(3000L);
                                System.err.println("执行结束:" + n);
                            } catch (InterruptedException e) {
                                System.out.println("异常：" + e.getMessage());
                            }
                        }
                    });

                }
            }
        }).start();


        Thread.sleep(5000L);
        pool.shutdown();
        System.out.println(">>>>>>>> 执行了shutdown方法");
        System.out.println("isShutdown:" + pool.isShutdown());

  /*      Thread.sleep(5000L);
        List<Runnable> taskList = pool.shutdownNow();
        System.out.println(">>> 调用了shutdownNow");
        System.out.println("isShutdown属性" + pool.isShutdown());
        System.out.println("未结束的任务有：" + taskList.size());*/

/*        System.out.println("准备执行awaitTermination" );
        pool.awaitTermination(5, TimeUnit.SECONDS);
        System.out.println("执行了awaitTermination" );*/

    }



}
