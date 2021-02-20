package com.dongnaoedu.network.spring.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heian
 * @date 2021/2/17 11:56 上午
 * @description 对于单例bean实行预先加载策略
 */
public class SingletonBeanPreBuildFactory extends DefaultBeanFactory{

    //存放那些beanNames
    private List<String> beanNames = new ArrayList<>();

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        super.registerBeanDefinition(beanName, beanDefinition);
        synchronized (beanNames){
            beanNames.add(beanName);
        }
    }

    public void preInstantiateSingletons() throws Exception {
        synchronized (beanNames){
            //将存放在单例集合中的bean预先加载好
            for (String beanName : beanNames) {
                BeanDefinition definition = this.getBeanDefinition(beanName);
                if (definition.isSingleton()){
                    this.getBean(beanName);
                    System.out.println("preInstantiate: name=" + beanName + " " + definition);
                }
            }

        }
    }
}
