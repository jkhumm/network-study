package com.dongnaoedu.network.spring.bean.sourseCodeTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * @author heian
 * @date 2022/4/1 12:03 下午
 * @description
 */
@Service
public class Test {

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void test(){
        A bean = applicationContext.getBean(A.class);
        System.out.println(bean.toString());
        // getBean --> doGetBean --> createBean --> doCreateBean
    }

    public static void main(String[] args) {
        // 冒泡排序
        int[] array = new int[]{1,2,3};

    }

    public void func(){
        // 选择
    }

}
