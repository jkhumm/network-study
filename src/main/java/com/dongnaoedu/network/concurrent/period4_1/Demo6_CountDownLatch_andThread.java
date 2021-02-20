package com.dongnaoedu.network.concurrent.period4_1;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

//利用CountDownLatch获取线程执行结果
public class Demo6_CountDownLatch_andThread {

    public static void main(String args[]) throws InterruptedException {

        Map<Thread, Integer> map = new HashMap<>();

        CountDownLatch ctl = new CountDownLatch(10);

        for (int i=0; i<10;i++){
            new Thread(){
                @Override
                public void run() {
                    int second = new Random().nextInt(6) + 1;
                    LockSupport.parkNanos(1000 * 1000 * 1000 * second);

                    //思考：如何让外界接收到，我生成的随机数呢？
                    Random random = new Random();
                    int num = random.nextInt();

                    map.put(Thread.currentThread(), num);

                    ctl.countDown();
                }
            }.start();
        }


        ctl.await();
        for (Integer num1 : map.values()){
            System.out.println(num1);
        }



    }






}

