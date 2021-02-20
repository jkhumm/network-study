package com.dongnaoedu.network.spring.bean;

import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author heian
 * @date 2021/2/15 2:43 下午
 * @description Bean定义接口,告诉工厂如何创建某类bean
 */
public interface BeanDefinition {

    String SCOPE_SINGLETON = "singleton";
    String SCOPE_PROTOTYPE = "prototype";


    /**
     * 获取bean的类名 构造函数和静态工厂创建对象所需
     */
    Class<?> getBeanClass();
    void setBeanClass(Class<?> beanClass);

    String getScope();
    void setScope(String scope);
    boolean isSingleton();
    boolean isPrototype();

    String getFactoryMethodName();//获取个工厂(静态和动态)方法的名字
    //成员工厂需要用到，因为通过此name获取factoryBean 来invoke对应bean
    void setFactoryMethodName(String factoryMethodName);


    String getFactoryBeanName();
    void setFactoryBeanName(String factoryBeanName);


    default boolean validate(){
        //没有对应bean的class信息，意味着只能提供成员工厂构建
        if (getBeanClass() == null){
            if (StringUtils.isBlank(getFactoryBeanName()) || StringUtils.isBlank(getFactoryMethodName())){
                return false;
            }
        }
        //class存在情况,还指定FactoryBeanName构建对象方式冲突
        if (getBeanClass() != null && StringUtils.isNotBlank(getFactoryBeanName())){
            return false;
        }
        return true;
    }

    /**
     * 类对，对象交给IOC容器管理，类对象的生命周期还有事情要做
     * 1.创建对象后可能需要进行一些初始化
     * 2.对象在销毁时肯需要进行一定的特定销毁的逻辑（如资源释放）
     * 3.bean定义中提供让用户指定初始化、销毁的方法
     * 4.对bean工厂提供getInitMethodName() getDestroyMethodName()
     */
    void setInitMethod(String initMethod);
    String getInitMethod();

    void setDestroyMethod(String destroyMethod);
    String getDestroyMethod();

    //获取你所需要赋值的成员参数 名称
    List<?> getConstructorArgumentValues();
    void setConstructorArgumentValues(List<?> constructorArgumentValues);

    //获取当前定义bean的构造参数  当实多例bean时，缓存起来省得你通过方法找那个构造函数
    Constructor getConstructor();
    void setConstructor(Constructor constructor);

    Method getFactoryMethod();
    void setFactoryMethod(Method factoryMethod);

}
