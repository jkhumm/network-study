package com.dongnaoedu.network.design.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理类具体的执行逻辑
 */
public class AnimalsProxyInvocationHandler implements InvocationHandler {

    private Object target;//目标对象  众多不愿意暴露的子类

    public AnimalsProxyInvocationHandler(Object target) {
        this.target = target;
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws InvocationTargetException, IllegalAccessException {
        before();
        Object invoke = method.invoke(target,args);//返回执行方法的结果
        after();
        return invoke;
    }

    public void before(){
        System.out.println("主人发出命令");//前置
    }

    public void after(){
        System.out.println("获得奖励");//后置
    }

    public static void main(String[] args) throws Exception{
        //目标类必须实现接口否则会抛出类转化异常
        //System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        Dog dogTarget = new Dog();
        //类加载器、代理类的接口数组（Dog类实现接口的数组）、代理业务类
        Animals proxy = (Animals)Proxy.newProxyInstance(dogTarget.getClass().getClassLoader(), dogTarget.getClass().getInterfaces(),
                new AnimalsProxyInvocationHandler(dogTarget));
        //重新生成一个子类进行接口方法的调用
        proxy.eat();
        //ProxyUtils.generateClassFile(proxy.getClass().getName(),dogTarget.getClass().getInterfaces());
    }

}
