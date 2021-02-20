package com.dongnaoedu.network.spring.bean;

import lombok.Getter;

/**
 * @author heian
 * @date 2021/2/17 3:00 下午
 * @description 成员变量为ioc容器的依赖引用
 */
@Getter
public class BeanReference {

    private String beanName;

    public BeanReference(String beanName) {
        this.beanName = beanName;
    }
}
