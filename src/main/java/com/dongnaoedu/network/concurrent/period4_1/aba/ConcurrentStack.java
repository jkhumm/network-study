package com.dongnaoedu.network.concurrent.period4_1.aba;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.locks.LockSupport;

public class ConcurrentStack {
    // top cas无锁修改
    //AtomicReference<Node> top = new AtomicReference<Node>();
    AtomicStampedReference<Node> top =
            new AtomicStampedReference<>(null, 0);

    public void push(Node node) { // 入栈
        Node oldTop;
        int v;
        do {
            v = top.getStamp();
            oldTop = top.getReference();
            node.next = oldTop;
        }
        while (!top.compareAndSet(oldTop, node, v, v+1)); // CAS 替换栈顶
    }


    // 出栈 -- 取出栈顶 ,为了演示ABA效果， 增加一个CAS操作的延时
    public Node pop(int time) {

        Node newTop;
        Node oldTop;
        int v;

        do {
            v = top.getStamp();
            oldTop = top.getReference();
            if (oldTop == null) {   //如果没有值，就返回null
                return null;
            }
            newTop = oldTop.next;
            if (time != 0) {    //模拟延时
                try {
                    TimeUnit.SECONDS.sleep(time);
                }catch (Exception e){

                }
            }
        }
        while (!top.compareAndSet(oldTop, newTop, v, v+1));     //将下一个节点设置为top
        return oldTop;      //将旧的Top作为值返回
    }
}
