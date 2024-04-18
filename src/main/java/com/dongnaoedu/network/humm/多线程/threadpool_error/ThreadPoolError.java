package com.dongnaoedu.network.humm.多线程.threadpool_error;

import lombok.Data;

import java.util.concurrent.*;

/**
 * @author heian
 * @date 2023/10/22 16:24
 * @description 线程池出现异常
 */
public class ThreadPoolError {


    public static void main(String[] args) throws Exception {

        //创建一个线程池
        ExecutorService executorService= Executors.newFixedThreadPool(1);

        //当线程池抛出异常后 submit无提示，其他线程继续执行
        Future<?> future = executorService.submit(new task("我是submit"));
        Object o = future.get();
        TimeUnit.SECONDS.sleep(1);

        //当线程池抛出异常后 execute抛出异常，其他线程继续执行新任务
       // executorService.execute(new task("我是execute"));

        executorService.shutdown();
    }

}
//任务类
@Data
class task implements  Callable<String> {

    private String str;

    public task(String str) {
        this.str = str;
    }


    @Override
    public String call() throws Exception {
        System.out.println("进入了task方法！！！" + str);
        return "xxx";
    }
}