package com.dongnaoedu.network.spring.bean;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author heian
 * @date 2021/2/15 3:37 下午
 * @description 通过反射机制实现工厂生产实例化bean
 */
public class DefaultBeanFactory implements BeanFactory,BeanDefinitionRegistry, Closeable {

    //缓存你要定义的 beanDefinition
    private Map<String,BeanDefinition> map = new ConcurrentHashMap<>();
    //缓存你要定义的 bean
    private Map<String,Object> beanMap = new ConcurrentHashMap<>();

    private Set<String> buildingBeans = Collections.newSetFromMap(new ConcurrentHashMap());


    @Override
    public Object getBean(String beanName) throws Exception{
        BeanDefinition bd = map.get(beanName);
        Object bean = doGetBean(beanName);

        //完成属性注入
        setPropertyDIValues(bd,bean);

        //开始bean的生命周期
        if (StringUtils.isNotBlank(bd.getInitMethod())){
            Method method = bean.getClass().getMethod(bd.getInitMethod(), null);
            method.invoke(bean,null);
        }
        return bean;
    }

    protected Object doGetBean(String beanName) throws Exception {
        Objects.requireNonNull(beanName,"beanName 不为空");
        //先去缓存中判断对象是否已经创建完成
        Object bean = beanMap.get(beanName);
        if (bean != null){
            return bean;
        }
        //创建对象的方式有三种：构造函数、静态工厂、成员工厂
        BeanDefinition beanDefinition = map.get(beanName);
        Objects.requireNonNull(beanDefinition,"beanDefinition 不存在");
        Class<?> beanClass = beanDefinition.getBeanClass();
        // 记录正在创建的Bean
        Set<String> ingBeans = this.buildingBeans;
        // 检测循环依赖
        if(ingBeans.contains(beanName)){
            throw new Exception(beanName + " 循环依赖！" + ingBeans);
        }
        // 记录正在创建的Bean
        ingBeans.add(beanName);
        if (beanClass != null){
            //构造函数
            if (StringUtils.isBlank(beanDefinition.getFactoryMethodName())){
                bean = createBeanByConstructor(beanDefinition);
            }else {
                //提供静态工厂创建对象  （知道工厂类名和方法名即可创建对象）
                bean = createBeanByStaticFactory(beanDefinition);
            }
        }else {
            //成员工厂构建对象 (不是工厂类  应该是工厂bean名FactoryBean + 工厂方法名)
            bean = createBeanByFactoryBean(beanDefinition);
        }
        // 实例创建完成后进行删除
        ingBeans.remove(beanName);
        // 对单例bean的处理
        if (beanDefinition.isSingleton()){
            beanMap.put(beanName,bean);
        }
        return bean;
    }

    //通过构造方法产生bean
    private Object createBeanByConstructor(BeanDefinition bd) throws Exception {
        Object instance = null;
        if(CollectionUtils.isEmpty(bd.getConstructorArgumentValues())) {
            instance = bd.getBeanClass().newInstance();
        }else{
            Object[] args = getRealValues(bd.getConstructorArgumentValues());
            if(args == null) {
                instance = bd.getBeanClass().newInstance();
            }else {
                return determineConstructor(bd, args).newInstance(args);
            }
        }
        return instance;
    }

    //通过静态工厂产生bean
    private Object createBeanByStaticFactory(BeanDefinition bd) throws Exception {
        Object[] realArgs = getRealValues(bd.getConstructorArgumentValues());
        Method method = determineFactoryMethod(bd, realArgs,bd.getBeanClass());
        Class<?> type = bd.getBeanClass();
        return method.invoke(type, realArgs);
    }

    //通过普通工厂拿到对应的bean
    private Object createBeanByFactoryBean(BeanDefinition bd) throws Exception {
        String factoryBeanName = bd.getFactoryBeanName();
        Object factoryBean = doGetBean(factoryBeanName);//静态工厂产生的FactoryBean
        if (factoryBean != null){
            Object[] realArgs = getRealValues(bd.getConstructorArgumentValues());
            Method method = determineFactoryMethod(bd, realArgs,factoryBean.getClass());
            return method.invoke(factoryBean, realArgs);
        }
        return null;
    }

    private Method determineFactoryMethod(BeanDefinition bd, Object[] args, Class<?> type) throws Exception {
        if (type == null) {
            type = bd.getBeanClass();
        }
        String methodName = bd.getFactoryMethodName();
        if(args  == null) {
            return type.getMethod(methodName, null);
        }
        Method m = null;
        // 对于原型bean,从第二次开始获取bean实例时，可直接获得第一次缓存的构造方法。
        m = bd.getFactoryMethod();
        if (m != null) {
            return m;
        }
        // 根据参数类型获取精确匹配的方法
        Class[] paramTypes = new Class[args.length];
        int j = 0;
        for (Object p : args) {
            paramTypes[j++] = p.getClass();
        }
        try{
            m = type.getMethod(methodName, paramTypes);
        }catch (NoSuchMethodException e){
            // 不做任何处理
            m = null;
        }
        if (m == null) {
            // 判断逻辑：先判断参数数量，再依次比对形参类型与实参类型
            outer: for (Method m0 : type.getMethods()) {
                if (!m0.getName().equals(methodName)) {
                    continue;
                }
                Class<?>[] paramterTypes = m0.getParameterTypes();
                if (paramterTypes.length == args.length) {
                    for (int i = 0; i < paramterTypes.length; i++) {
                        if (!paramterTypes[i].isAssignableFrom(args[i].getClass())) {
                            continue outer;
                        }
                    }
                    m = m0;
                    break outer;
                }
            }
        }
        if (m == null){
            throw new Exception("不存在对应的构造方法！" + bd);
        }
        // 对于原型bean,可以缓存找到的方法，方便下次构造实例对象。在BeanDefinition中获取设置所用方法的方法。
        if (bd.isPrototype()) {
            bd.setFactoryMethod(m);
        }
        return m;
    }

    private Constructor determineConstructor(BeanDefinition bd, Object[] args) throws Exception {
        Constructor ct = null;
        if(args == null) {return bd.getBeanClass().getConstructor(null);}
        Class<?>[] paramType = new Class[args.length];
        // 对于原型Bean，从第二次开始获取Bean实例时，可以直接从第一次缓存中获取构造方法
        ct = bd.getConstructor();
        if(ct != null) {return ct;}
        // 根据参数类型获取构造方法
        int j = 0;
        for(Object p : args) {
            paramType[j++] = p.getClass();
        }
        ct = bd.getBeanClass().getConstructor(paramType);
        if(ct == null) {
            Constructor<?>[]  cts = bd.getBeanClass().getConstructors();
            // 判断逻辑：先判断参数数量，依次判断形参跟实参进行类型匹配
            outer: for(Constructor<?> c : cts) {
                Class<?>[] paramterTypes = c.getParameterTypes();
                if(paramterTypes.length == args.length) {
                    for(int i = 0; i < paramterTypes.length; i++) {
                        //判断构造函数的形参是否和我们实参数一致
                        if(!paramterTypes[i].isAssignableFrom(args[i].getClass())) {
                            continue outer;
                        }
                    }
                    ct = c;
                    break outer;
                }
            }
        }
        if (ct == null){
            throw new Exception("找不到对应的构造方法：" + bd);
        }
        if(bd.isPrototype()) {
            bd.setConstructor(ct);
        }
        return ct;
    }

    /**
     * 字段中包含其它引用类型字段：如ioc的bean字段、集合等
     */
    private Object[] getRealValues(List<?> args) throws Exception {
        if(CollectionUtils.isEmpty(args)) {return null;}
        Object[] values = new Object[args.size()];

        Object v = null;
        for(int i = 0; i < args.size(); i++){
            Object rv = args.get(i);
            if(rv == null) {
                v = null;
            }else if (rv instanceof  BeanReference){
                v = doGetBean(((BeanReference) rv).getBeanName());
            }else if (rv instanceof Object[]) {
                // TODO 处理集合中的bean引用
            } else if (rv instanceof Collection) {
                // TODO 处理集合中的bean引用
            } else if (rv instanceof Properties) {
                // TODO 处理properties中的bean引用
            } else if (rv instanceof Map) {
                // TODO 处理Map中的bean引用
            } else {
                v = rv;
            }
            values[i] = v;
        }
        return values;
    }
    private void setPropertyDIValues(BeanDefinition bd, Object instance) throws Exception {
        if (CollectionUtils.isEmpty(bd.getPropertyValues())) {
            return;
        }
        for (PropertyValue pv : bd.getPropertyValues()) {
            if (StringUtils.isBlank(pv.getName())) {
                continue;
            }
            Class<?> clazz = instance.getClass();
            Field p = clazz.getDeclaredField(pv.getName());
            p.setAccessible(true);
            Object rv = pv.getValue();
            Object v = null;
            if (rv == null) {
                v = null;
            } else if (rv instanceof BeanReference) {
                v = this.doGetBean(((BeanReference) rv).getBeanName());
            } else if (rv instanceof Object[]) {
                // TODO 处理集合中的bean引用
            } else if (rv instanceof Collection) {
                // TODO 处理集合中的bean引用
            } else if (rv instanceof Properties) {
                // TODO 处理properties中的bean引用
            } else if (rv instanceof Map) {
                // TODO 处理Map中的bean引用
            } else {
                v = rv;
            }
            p.set(instance, v);
        }
    }

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        Objects.requireNonNull(beanName,"注册的bean需要指定 beanName");
        Objects.requireNonNull(beanDefinition,"注册的bean需要指定 beanDefinition");
        if(!beanDefinition.validate()) {
            throw new RuntimeException("名字为["+beanName+"]的bean定义不合法："+beanDefinition);
        }
        if(containsBeanDefinition(beanName)) {
            throw new RuntimeException("名字为["+beanName+"]已存在："+getBeanDefinition(beanName));
        }
        map.put(beanName,beanDefinition);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) {
        return this.map.get(beanName);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return map.containsKey(beanName);
    }

    @Override
    public void close() throws IOException {
        //针对单例bean进行的销毁方法
        map.forEach((s, beanDefinition) -> {
            if (beanDefinition.isSingleton() && StringUtils.isNotBlank(beanDefinition.getDestroyMethod())){
                try {
                    Object bean = beanMap.get(s);
                    if (bean == null){
                        return;//不会终止循环
                    }
                    Method method = bean.getClass().getMethod(beanDefinition.getDestroyMethod(), null);
                    method.invoke(bean,null);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //JVM关闭后会执行所有实现了Closeable接口的方法，也就是我们这个实现的close方法
    public void destroy(){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            //把所有实现closeable接口的参数都加上此代码
            try {
                close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
    }

}
