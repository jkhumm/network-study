package com.dongnaoedu.network.concurrent.period8.queue;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

public class Demo4_PriorityBlockingQueue2 {
    public static void main(String args[]){
        // 可以设置比对方式
        PriorityBlockingQueue<String> queue = new PriorityBlockingQueue<>(5,
                new Comparator<String>() {
            @Override //
            public int compare(String o1, String o2) {
                int num1 = new Integer(o1);
                int num2 = new Integer(o2);

                if (num1 > num2)
                    return -1;
                else if (num1 == num2)
                    return 0;
                else
                    return 1;
            }
        });

        queue.put("48");
        queue.put("01");
        queue.put("12");
        queue.put("27");
        queue.put("31");

        for (;queue.size()>0;){
            try {
                System.out.println(queue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

