package com.dongnaoedu.network.exame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author heian
 * @date 2021/3/12 9:46 下午
 * @description 格力笔试题
 */
public class Demo1 {


    public static void func(){
        Thread t = new Thread(() -> {
            System.out.println("runnable  " + Thread.currentThread().getName()) ;
        },"测试线程");

        t.run();
        System.out.println("java is come");

        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList("a","b"));
        ArrayList<String> arrayList2 = new ArrayList<>(Arrays.asList("c","d"));

        List<Object> collect = arrayList.stream().flatMap(o -> arrayList2.stream()).collect(Collectors.toList());
    }


    public static void main(String[] args) {
        func();
    }

}
