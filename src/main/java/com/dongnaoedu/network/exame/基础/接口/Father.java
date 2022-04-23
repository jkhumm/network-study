package com.dongnaoedu.network.exame.基础.接口;

/**
 * @author heian
 * @date 2022/4/19 1:46 下午
 * @description 点链科技二面
 */
public interface Father {


    public void fun1();

    abstract void fun2();


    default void fun3() {
        System.out.println("fun3");
    }

    static void fun4() {
        System.out.println("fun4");
    }

}
