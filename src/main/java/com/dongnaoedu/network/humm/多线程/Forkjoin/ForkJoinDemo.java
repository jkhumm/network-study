package com.dongnaoedu.network.humm.多线程.Forkjoin;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.*;

/**
 * @author Heian
 * @time 19/07/28 22:24
 * @description： 理解forkjoin（线程池 +任务拆分 ）的原理
 */
public class ForkJoinDemo {

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
    public static String doRequest(String url) throws InterruptedException{
        TimeUnit.MILLISECONDS .sleep (100);
        return "访问的网址："+url + "\n";
    }

    //设置我们需要的任务，从多少到多少位一组算一个任务
    static  class TaskGroup implements Callable<String>{
        private int startIndex;
        private int endIndex;

        public TaskGroup(int startIndex,int endIndex) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        public String call() throws Exception {
            String sb = "";
            for (int i = startIndex-1; i <=endIndex-1 ; i++) {
                String s = doRequest (urls.get (i));
                sb+=s;
            }
            return sb;
        }
    }


    /**
     *  拆分任务
     * @param pageSize 按照多大为一组，拆分任务
     */
    public static void splitTask(int pageSize) throws ExecutionException, InterruptedException {
        int size = urls.size ();
        int groupCount = size/pageSize + 1; //   9/10 = 0
        System.out.println ("任务大小为：" + size + ",分成" + groupCount + "组来处理");
        ExecutorService executorService = Executors.newFixedThreadPool (4);
        ArrayList<Future<String>> list = new ArrayList<> ();
        long startTime = System.currentTimeMillis ();
        /*------------------任务分组逻辑-----------------------*/
        //因为可能最后一组会存在零星的，所以要单独拿出来
        for (int i = 1; i <= groupCount-1; i++) {
            int startPageNum = (i-1)*pageSize + 1;//起始页码 = （第几组-1）*pageSize +1 从1开始
            int endPageNum = pageSize*i;  //截止页码 = 第几组*pageSize
            System.out.println (startPageNum + ":" + endPageNum);
            Future<String> future = executorService.submit (new TaskGroup (startPageNum, endPageNum));
            list.add (future);
        }
        //零星  最后一组
        int startPageNum = (groupCount-1)*pageSize + 1;//起始页码 = （第几组-1）*pageSize +1 从1开始
        int endPageNum = size;  //截止页码 = 第几组*pageSize
        System.out.println (startPageNum + ":" + endPageNum);
        Future<String> future = executorService.submit (new TaskGroup (startPageNum, endPageNum));
        list.add (future);
        for (Future<String> item : list){
            //拿到每组任务的返回值（很长的网址拼接）
            System.out.println(item.get());//阻塞
        }
        System.out.println ("耗时为" + (System.currentTimeMillis ()-startTime) + "毫秒");

    }


    /**
     * 我们要做的就是模拟,将我们要访问的网址，拆分成多组任务，后交给线程池处理
     */
    public static void main(String[] args) throws Exception{
        //splitTask(10);
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<> ();
        list.add (1);
        list.add (2);
        list.add (3);
        list.add (4);
        list.remove (0);
        list.forEach (integer -> System.out.println (integer));

    }


}
