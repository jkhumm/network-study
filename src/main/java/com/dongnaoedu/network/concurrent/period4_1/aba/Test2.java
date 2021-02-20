package com.dongnaoedu.network.concurrent.period4_1.aba;

import java.util.concurrent.locks.LockSupport;

public class Test2 {
    public static void main(String[] args) throws InterruptedException {
        Stack2 stack = new Stack2();
        //ConcurrentStack stack = new ConcurrentStack();

        stack.push(new Node("B"));      //B入栈
        stack.push(new Node("A"));      //A入栈

        Thread thread1 = new Thread(() -> {
            Node node = null;
            node = stack.pop(2000);
            System.out.println(Thread.currentThread().getName() +" "+ node.toString());
            System.out.println(Thread.currentThread().getName() + ":done...");

        },"1线程");
        thread1.start();


        Thread thread2 = new Thread(() -> {
            LockSupport.parkNanos(1000 * 1000 * 3L);
            Node nodeB = null;      //取出B，之后B处于游离状态
                Node nodeA = stack.pop(0);      //取出A
                System.out.println(Thread.currentThread().getName()  +" "+  nodeA.toString());
                nodeB = stack.pop(0);
                System.out.println(Thread.currentThread().getName()  +" "+  nodeB.toString());

                stack.push(new Node("D"));      //D入栈
                stack.push(new Node("C"));      //C入栈
                stack.push(nodeA);                    //A入栈

            System.out.println(Thread.currentThread().getName() + ":done...");

        },"2线程");
        thread2.start();

        LockSupport.parkNanos(1000 * 1000 * 1000 * 5L);


        System.out.println("开始遍历Stack：");
        Node node = null;
        while ((node = stack.pop(0))!=null){
            System.out.println(node.value);
        }
    }
}
