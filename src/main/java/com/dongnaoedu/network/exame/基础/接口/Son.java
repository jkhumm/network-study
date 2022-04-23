package com.dongnaoedu.network.exame.基础.接口;

/**
 * @author heian
 * @date 2022/4/19 1:47 下午
 * @desc 抽象类则可以不用重写父类接口任何方法
 */
public class Son implements Father {


    @Override
    public void fun1() {
        System.out.println("我不是抽象类，必须重写父类普通方法");
    }

    @Override
    public void fun2() {
        System.out.println("重写父类抽象方法");
    }

    @Override
    public void fun3() {
        System.out.println("重写父类default方法,也可以不重写");
    }


}
