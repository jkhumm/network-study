package com.dongnaoedu.network.concurrent.period1;

import java.util.Map;

public class Demo5_ThreadLocal {
    /** threadLocal变量，每个线程都有一个副本，互不干扰 */

    public static ThreadLocal<String> value = new ThreadLocal<String>();

    public static void main(String[] args) throws Exception {

        new Thread(new Runnable() {
            public void run() {
                value.set("111111111");

                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(value.get());
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(1000L);
                    System.out.println(value.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }).start();



    }

    public static Map<Thread, String> map;

}
