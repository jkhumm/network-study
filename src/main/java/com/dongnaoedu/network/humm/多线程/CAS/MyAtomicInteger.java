package com.dongnaoedu.network.humm.多线程.CAS;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class MyAtomicInteger {

    private static Unsafe unsafe = null;
    private int i;
    private static long valueOffset;
    static {
        Class<Unsafe> unsafeClass = Unsafe.class;
        try {
            Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            unsafe = (Unsafe) unsafeField.get(unsafe);//无任何成员变量
            //指定要修改的字段
            Field iField = MyAtomicInteger.class.getDeclaredField("i");
            valueOffset = unsafe.objectFieldOffset(iField);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println("实例化MyAtomicInteger异常");
        }
    }




    public void add(){
        for (;;){
            //指定你要修改的对象和该对象的成员变量的偏移量,成员变量的旧值，成员变量的新值
            if (unsafe.compareAndSwapInt(this, valueOffset, i, i + 1)){
                //成功替换，则跳出循环，否则一直重试
                return;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        //开启10个线程去对int进行i++，加至10000次，看是否成功
        MyAtomicInteger myAtomicInteger = new MyAtomicInteger();
        IntStream.range(0,10).forEach(value -> {
            new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    myAtomicInteger.add();
                }
                System.out.println(Thread.currentThread().getName() + "done");
            }).start();
        });
        TimeUnit.SECONDS.sleep(1);
        System.out.println(myAtomicInteger.i);
    }

}
