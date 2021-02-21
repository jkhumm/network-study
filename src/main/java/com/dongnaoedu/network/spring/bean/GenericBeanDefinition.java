package com.dongnaoedu.network.spring.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author heian
 * @date 2021/2/15 3:32 下午
 * @description 通用的定义注册的实现
 */
public class GenericBeanDefinition implements BeanDefinition{

    private Class<?> beanClass;
    private String factoryMethodName;//静态或者成员的工厂方法名
    private String factoryBeanName;//成员工厂需要用到，因为通过此name获取factoryBean 来invoke对应bean
    private String scope = BeanDefinition.SCOPE_SINGLETON;
    private String initMethod;
    private String destroyMethod;

    private List<?> constructorArgumentValues;
    private Constructor constructor;//多例bean缓存该构造函数

    private Method factoryMethod;

    private List<PropertyValue> propertyValues;


    @Override
    public Class<?> getBeanClass() {
        return this.beanClass;
    }

    @Override
    public void setBeanClass(Class<?> beanClass) {
       this.beanClass = beanClass;
    }

    @Override
    public String getScope() {
        return this.scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean isSingleton() {
        return BeanDefinition.SCOPE_SINGLETON.equals(scope);
    }

    @Override
    public boolean isPrototype() {
        return BeanDefinition.SCOPE_PROTOTYPE.equals(scope);
    }

    @Override
    public String getFactoryMethodName() {
        return this.factoryMethodName;
    }

    @Override
    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }

    @Override
    public String getFactoryBeanName() {
        return this.factoryBeanName;
    }

    @Override
    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    @Override
    public void setInitMethod(String initMethod) {
        this.initMethod = initMethod;
    }

    @Override
    public String getInitMethod() {
        return this.initMethod;
    }

    @Override
    public void setDestroyMethod(String destroyMethod) {
        this.destroyMethod = destroyMethod;
    }

    @Override
    public String getDestroyMethod() {
        return this.destroyMethod;
    }

    @Override
    public List<?> getConstructorArgumentValues() {
        return this.constructorArgumentValues;
    }

    @Override
    public void setConstructorArgumentValues(List<?> constructorArgumentValues) {
        this.constructorArgumentValues = constructorArgumentValues;
    }

    @Override
    public Constructor getConstructor() {
        return this.constructor;
    }

    @Override
    public void setConstructor(Constructor constructor) {
        this.constructor = constructor;
    }

    @Override
    public Method getFactoryMethod() {
        return this.factoryMethod;
    }

    @Override
    public void setFactoryMethod(Method factoryMethod) {
        this.factoryMethod = factoryMethod;
    }

    @Override
    public List<PropertyValue> getPropertyValues() {
        return this.propertyValues;
    }

    @Override
    public void setPropertyValues(List<PropertyValue> propertyValues) {
        this.propertyValues = propertyValues;
    }
}
