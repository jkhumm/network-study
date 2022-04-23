package com.dongnaoedu.network.exame;

/**
 * @author heian
 * @date 2022/4/11 7:33 下午
 * @description -227-128  享元模式
 */
public class IntegeerTest {


    public static void main(String[] args) {
        Integer a = 100;
        Integer b = 100;
        System.out.println(a==b); // true
        Integer c = 1000;
        Integer d = 1000;
        System.out.println(c==d); // false


    }


}
