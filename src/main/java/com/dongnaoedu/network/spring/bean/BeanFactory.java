package com.dongnaoedu.network.spring.bean;

/**
 * @author heian
 * @date 2021/2/15 2:38 下午
 * @description bean工厂接口
 */
public interface BeanFactory {

    Object getBean(String beanName) throws Exception;

}
