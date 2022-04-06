package com.dongnaoedu.network.humm.多线程.threadLocal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.locks.LockSupport;

/**
 * @author heian
 * @create 2021-01-17-10:47 下午
 */
public class InThreadLocalContext {

    private static InheritableThreadLocal<ContextObject> inTl = new InheritableThreadLocal<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class ContextObject {
        String name;
        int value;
    }

    public static void main(String[] args) {

        ContextObject context = new ContextObject();
        context.setName("father");
        context.setValue(10);
        inTl.set(context);

        new Thread(() -> {
            // 获取线程的上下文
            ContextObject contextObject = inTl.get();
            if (contextObject != null) {
                System.out.println("son-1:" + contextObject.getName() + "," + contextObject.getValue());
            }
            inTl.set(new ContextObject("son-1",21));
            System.out.println("son-1:" + inTl.get().getName() + "," + inTl.get().getValue());
        }).start();

        LockSupport.parkNanos(1000*1000*1000L);

        System.out.println("-----");
        System.out.println(inTl.get().getName() + "," + inTl.get().getValue());
    }
}
/**
 * son-1:father,10
 * son-1:son-1,21
 * -----
 * father,10
 */