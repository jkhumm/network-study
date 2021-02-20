package com.dongnaoedu.network.spring.bean;

/**
 * @author heian
 * @date 2021/2/15 2:44 下午
 * @description Bean定义的注册接口,完成bean工厂和bean定义之间的桥梁
 */
public interface BeanDefinitionRegistry {

    /**
     * 向工厂注册定义的bean
     * @param beanDefinition 你想要的注册的bean的信息
     */
    void registerBeanDefinition(String beanName,BeanDefinition beanDefinition);

    /**
     * 获取已经注册的bean
     */
    BeanDefinition getBeanDefinition(String beanName);

    /**
     * 是否包含了已经定义的bean
     */
    boolean containsBeanDefinition(String beanName);
}
