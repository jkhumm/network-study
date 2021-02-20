package com.dongnaoedu.network.concurrent.period5;

/*
1、基本使用 用于实例方法、用于静态方法 隐式指定锁对象
2、用于代码块时，显示指定锁对象
3、锁的作用域
4、引申，如果是多个进程，怎么办？
 */

public class Demo3_Synchronized {
    public static void main(String args[]){
        Counter ct1 = new Counter();
        Counter ct2 = new Counter();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //ct1.add();
                    Counter.static_add();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //ct1.add();
                    Counter.static_add();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }





}


class Counter{

    private static Student lock = new Student();

    private static int i = 0;

    // synchronized (this){}
    public synchronized void add() throws InterruptedException {
        System.out.println( Thread.currentThread().getName() + " done ...");
        i++;
        Thread.currentThread().sleep(60000L);
    }

    //synchronized (Counter.class)
    public static synchronized void static_add() throws InterruptedException {
        System.out.println( Thread.currentThread().getName() + " done ...");
        i++;
        Thread.currentThread().sleep(60000L);
    }





    public void add_block(){
        synchronized (lock){
            i++;
        }
    }

    public static void add_block01(){
        synchronized (lock){
            i++;
        }
    }









}