package com.dongnaoedu.network.concurrent.period4_1.aba;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

// 实现一个 栈（后进先出）
public class Stack {
    // top cas无锁修改
    AtomicReference<Node> top = new AtomicReference<Node>();

    public void push(Node node) { // 入栈
        Node oldTop;
        do {
            oldTop = top.get();
            node.next = oldTop;
        }
        while (!top.compareAndSet(oldTop, node)); // CAS 替换栈顶
    }


    // 出栈 -- 取出栈顶 ,为了演示ABA效果， 增加一个CAS操作的延时
    public Node pop(int time)  {

        Node newTop;
        Node oldTop;
        do {
            oldTop = top.get();
            if (oldTop == null) {   //如果没有值，就返回null
                return null;
            }
            newTop = oldTop.next;//引用没有更新
            if (time != 0) {    //模拟延时
                try {
                    TimeUnit.SECONDS.sleep(time);
                }catch (InterruptedException e){
                }
            }
        }
        //将下一个节点设置为top  oldTop:cda 但newTop:ba
        while (!top.compareAndSet(oldTop, newTop));
        return oldTop;      //将旧的Top作为值返回
    }
}