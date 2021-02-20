package com.dongnaoedu.network.design.proxy;


/**
 * 代理类具体的执行逻辑
 */
public class AnimalsStaticProxy implements Animals {

    private Animals animals;

    public AnimalsStaticProxy(Animals animals) {
        this.animals = animals;
    }

    @Override
    public void eat() {
        before();
        animals.eat();
        after();

    }

    @Override
    public void run() {
        before();
        animals.run();
        after();
    }

    public void before(){
        System.out.println("主人发出命令");//前置
    }

    public void after(){
        System.out.println("获得奖励");//后置
    }

    public static void main(String[] args) {
        Animals animals = new AnimalsStaticProxy(new Dog());
        animals.eat();
        animals.run();
    }

}
