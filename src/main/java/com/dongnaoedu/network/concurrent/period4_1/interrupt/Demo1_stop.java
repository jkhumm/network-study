package com.dongnaoedu.network.concurrent.period4_1.interrupt;

public class Demo1_stop {
    public static void main(String[] args) throws InterruptedException {
        MyThread thread = new MyThread();
        thread.start();

        Thread.sleep(1000);     // 休眠1秒，确保i变量自增成功
        thread.stop();

        while (thread.isAlive()) {}  // 确保线程已经终止
        thread.print();     // 输出结果
    }
}

class MyThread extends Thread {
    private int i = 0, j = 0;

    @Override
    public void run() {
        synchronized (this) {
            ++i;
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ++j;
        }
        System.out.println("锁释放。。。");
    }

    public void print() {
        System.out.println("i=" + i + " j=" + j);
    }
}

