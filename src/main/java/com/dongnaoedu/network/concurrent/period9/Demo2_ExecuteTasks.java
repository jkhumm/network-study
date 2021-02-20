package com.dongnaoedu.network.concurrent.period9;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Demo2_ExecuteTasks {

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


    public static void main(String args[]) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis ();
        System.out.println (startTime);
        //当任务列表比较大、单个任务比较大的时候，我们要将任务进行拆分
        int size = urls.size();System.out.println("任务大小为：" +size);
        //一组任务有多大
        int groupSize = 10;
        //多少组任务
        int groupCount = (size -1) /groupSize +1;
        ExecutorService pool = Executors.newFixedThreadPool(3);
        List<Future> futures = new ArrayList<>();
        //任务拆分
        for (int groupIndex=0; groupIndex < groupCount -1; groupIndex++){
            int leftIndex = groupSize * groupIndex;
            int rightIndex = groupSize * (groupIndex + 1);

            ///System.out.println(leftIndex + ":" + rightIndex);
            Future<String> future = pool.submit(new Task(leftIndex, rightIndex));
            futures.add(future);
        }
        int leftIndex = groupSize * (groupCount -1);
        int rightIndex = size;
        //System.out.println(leftIndex + ":" + rightIndex);
        Future<String> future = pool.submit(new Task(leftIndex, rightIndex));
        futures.add(future);
        //收集任务结果
        for (Future<String> item : futures){
            System.out.println(item.get());//阻塞
        }
        long endTime = System.currentTimeMillis ();

        System.out.println ("耗时为：" +(endTime-startTime)/1000);


    }



    //模拟网络请求
    public static String doRequest(String url) throws InterruptedException{
        TimeUnit.SECONDS.sleep (1);
        return "访问的网址：" + url + "\n";
    }
   static class Task implements Callable<String>{
        private int start;
        private int end;
        public Task(int start, int end){
            this.start = start;
            this.end = end;
        }
        @Override
        public String call() throws Exception {
            String result = "";
            for (int i=start; i<end; i++){
                result += doRequest(urls.get(i));
            }
            return result;
        }
    }

}
