package com.dongnaoedu.network.design.proxy;

//要在狗吃东西之前加上 发出命令 和 获得奖励
public class Dog implements Animals {

    public void eat() {
        System.out.println("狗吃");
    }

    public void run() {
        System.out.println("狗跑");
    }




}
