package com.dongnaoedu.network.design.proxy;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class AnimalsProxyMethodInterceptor implements MethodInterceptor {

    private Object target;//目标对象  众多不愿意暴露的子类

    public AnimalsProxyMethodInterceptor(Object target) {
        this.target = target;
    }



    public void before(){
        System.out.println("主人发出命令");//前置
    }

    public void after(){
        System.out.println("获得奖励");//后置
    }

    @Override
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("**************** " + method.getName());
        before();
        Object invoke = method.invoke(target,args);//返回执行方法的结果
        after();
        return invoke;
    }


    public static void main(String[] args) throws Exception{
        //对类生成代理
        Enhancer enhancer = new Enhancer();//类似Proxy类
        enhancer.setCallback(new AnimalsProxyMethodInterceptor(new Dog()));
        enhancer.setSuperclass(Dog.class);//也可以写父类
        Dog proxyAnimals = (Dog) enhancer.create();
        proxyAnimals.eat();

        Enhancer enhancer2 = new Enhancer();
        enhancer2.setCallback(new AnimalsProxyMethodInterceptor(new Dog()));
        enhancer.setInterfaces(new Class[]{Animals.class});//接口代理
        Animals proxyAnimals2 = (Animals) enhancer.create();
        proxyAnimals2.eat();
    }

}
