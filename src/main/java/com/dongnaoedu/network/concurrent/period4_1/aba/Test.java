package com.dongnaoedu.network.concurrent.period4_1.aba;

import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) throws InterruptedException {
       // Stack stack = new Stack();
        ConcurrentStack stack = new ConcurrentStack();

        stack.push(new Node("A"));
        stack.push(new Node("B"));
        stack.push(new Node("C"));

        new Thread(() -> {
                System.out.println(Thread.currentThread().getName() + "运行");
                Node node = stack.pop(3);
                System.out.println(Thread.currentThread().getName()+"done...");
        },"线程A").start();

        TimeUnit.MILLISECONDS.sleep(500);

        new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "运行");
            Node nodeC = stack.pop(0); //取出C
            stack.pop(0);              //取出B，之后B处于游离状态,可能被回收
            stack.push(new Node("D"));//D入栈
            stack.push(nodeC);              //C入栈
            System.out.println(Thread.currentThread().getName()+"done...");
        },"线程B").start();
        //当活动线程数大于1，则暂停main线程
        while (Thread.activeCount() > 1) {
            Thread.yield();
        }
        System.out.println("开始遍历Stack：");
        Node node = null;
        while ((node = stack.pop(0))!=null){
            System.out.println(node.value);
        }
    }
}
