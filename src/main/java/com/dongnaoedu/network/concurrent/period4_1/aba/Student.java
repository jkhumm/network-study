package com.dongnaoedu.network.concurrent.period4_1.aba;

import java.util.concurrent.TimeUnit;

/**
 * @author heian
 * @create 2020-02-23-1:23 下午
 * @description
 */
public class Student {
    private Node node;


    public static void main(String[] args) {
        Student s = new Student();
        Node nodeA = new Node("A");
        Node nodeB = new Node("B");
        s.node = nodeA;
        s.node.next =  nodeB;
        //  A->B
        new Thread(() -> {
            try {
                System.out.println("A开始");
                TimeUnit.SECONDS.sleep(2);
                Node oldNode = s.node;
                Node newNode = s.node.next;
                System.out.println(oldNode.value);
                System.out.println(newNode.value);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },"A").start();

        new Thread(() -> {
            System.out.println("B开始");
            Node nodeC = new Node("C");
            nodeC.next = new Node("D");
            s.node = nodeC;
        },"B").start();


    }

}
