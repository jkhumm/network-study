package com.dongnaoedu.network.concurrent.period4_1;

import java.util.concurrent.locks.LockSupport;

public class Demo2_RunnableTest {
    public static void main(String args[]){

        Runnable cmd = new Task();
  /*
        cmd.run();
        System.out.println("5s 后才输出消息");

*/

        //说明Runable.run 是阻塞的，没有开启线程

        Thread th = new Thread(cmd);
        th.start();
        System.out.println("开启线程，实现异步，立即打印消息。。。");
    }


}

class Task implements Runnable{
    @Override
    public void run() {
        System.out.println("run 方法开始执行。。。");
        LockSupport.parkNanos(1000* 1000 * 1000 * 5L);
    }
}
