package com.dongnaoedu.network.concurrent.period2;

import java.util.concurrent.*;

public class Demo1_CallableTest  {

    static ThreadPoolExecutor pool =
            new ThreadPoolExecutor(1, 2, 5,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>() );
    
    public static void main(String args[]) throws ExecutionException, InterruptedException {
        //Runnable Test
        Future run_future = pool.submit(new MyRunnable());
        System.out.println("run_future: " + run_future.get());


        //Callable Test
        Future call_future = pool.submit(new MyCallable());
        System.out.println("call_future:" + call_future.get());



    }

}


class MyCallable implements Callable<Integer>{

    public Integer call() throws Exception {
        return 1;
    }
}


class MyRunnable implements Runnable{

    public void run() {

    }
}