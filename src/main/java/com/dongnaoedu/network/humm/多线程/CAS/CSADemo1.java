package com.dongnaoedu.network.humm.多线程.CAS;

import java.lang.reflect.Field;
import java.util.stream.IntStream;

/**
 * @author heian
 * @create 2020-02-22-4:14 下午
 * @description
 */
public class CSADemo1 {

    private static CSADemo1 sss = null;

    static {
        Field field = null;
        try {
            field = CSADemo1.class.getDeclaredField("sss");
            field.setAccessible(true);
            CSADemo1 o = (CSADemo1)field.get(null);
            System.out.println(o);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

    }




    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        //调试技巧
        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "1");
            System.out.println(Thread.currentThread().getName() + "2");
            System.out.println(Thread.currentThread().getName() + "3");
        },"线程1").start();

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "4");
            System.out.println(Thread.currentThread().getName() + "5");
            System.out.println(Thread.currentThread().getName() + "6");
        },"线程2").start();

        System.out.println(Thread.currentThread().getName() + "7");

        IntStream.range(0,10).forEach(value -> {
            System.out.println(value);
        });

    }


}
