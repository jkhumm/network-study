package com.dongnaoedu.network.humm.多线程.Forkjoin;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author Heian
 * @time 19/07/28 22:24
 * @description： 理解forkjoin（线程池 +任务拆分 ）的原理
 */
public class ForkJoinTest {

    //自定义的任务
    static ArrayList<String> urls = new ArrayList<String>(){
        {
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
            add("http://www.sina.com");
            add("http://www.baidu.com");
        }
    };

    //模拟网络请求，假设这里请求耗时为100毫秒
    public static String doRequest(String url,int index) throws InterruptedException{
        TimeUnit.MILLISECONDS .sleep (100);
        return index + "-访问的网址："+url + "\n";
    }

    //本质是一个线程池,默认的线程数量:CPU的核数
    static ForkJoinPool forkJoinPool = new ForkJoinPool (Runtime.getRuntime().availableProcessors(),//我是4核
            ForkJoinPool.defaultForkJoinWorkerThreadFactory, null, false);

    /**
     * 现在将一个大任务分成多组任务，而分组的边界有自己设定，直到边界不能再细分
     */
    static class Job extends RecursiveTask<String>{
        private List<String> list;//需要拆分的任务
        private int start;
        private int end;

        public Job(List<String> list, int start, int end) {
            this.list = list;
            this.start = start;
            this.end = end;
        }

        //不断的拆分任务，直到任务数小于10才不拆分，
        @Override
            protected String compute() {
            int taskSize = end - start;//得到任务的大小
            if (taskSize<=10){
                //先把任务拆分好了，在将各组任务执行
                System.out.println ("小于10" + Thread.currentThread ().getName ());
                String result = "";
                for (int i = start; i <end ; i++) {
                    try {
                        result += doRequest (urls.get (i),i);
                    } catch (InterruptedException e) {
                        e.printStackTrace ();
                    }
                }
                return result;
            }else {
                //拆分任务
                int x = (start + end)/2;//将任务分成两份  奇数：3--> 1，2
                System.out.println (x+ Thread.currentThread ().getName ());
                //起三个线程去分解这些任务
                Job job1 = new Job (urls,start,x);
                ForkJoinTask<String> fork = job1.fork ();
                Job job2 = new Job (urls,x,end);
                ForkJoinTask<String> fork1 = job2.fork ();
                //固定写法  类似于语法
                String result = "";
                result += job1.join ();
                result += job2.join ();
                return result;
            }

        }
    }


    /**
     * 我们要做的就是模拟,将我们要访问的网址，拆分成多组任务，后交给线程池处理
     */
    public static void main(String[] args) throws Exception{
        long statrTime = System.currentTimeMillis ();
        Job job = new Job (urls,0,urls.size ());
        ForkJoinTask<String> result = forkJoinPool.submit (job);
        System.out.println (result.get ());
        System.out.println ("耗时：" + (System.currentTimeMillis ()-statrTime));

    }


}
