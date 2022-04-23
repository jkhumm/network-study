package com.dongnaoedu.network.exame.基础.线程;

/**
 * @author heian
 * @date 2021/3/12 9:46 下午
 * @description 格力笔试题
 */
public class Geli {


    public static void func(){
        Thread t = new Thread(() -> {
            System.out.println("runnable  " + Thread.currentThread().getName()) ;
        },"测试线程");

        t.run();
        System.out.println("java is come");

    }


    public static void main(String[] args) {
        func();
    }

}
