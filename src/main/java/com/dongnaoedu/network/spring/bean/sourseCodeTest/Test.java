package com.dongnaoedu.network.spring.bean.sourseCodeTest;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author heian
 * @date 2022/4/1 12:03 下午
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
        ArrayList<TestVo> testVos = new ArrayList<>();
        TestVo a = new TestVo("a","a");
        TestVo b = new TestVo("b","b");
        testVos.add(a);
        testVos.add(b);
        Map<String, List<TestVo>> collect = testVos.stream().collect(Collectors.groupingBy(TestVo::getAge));
        Map<String, String> collect1 = testVos.stream().collect(Collectors.toMap(TestVo::getAge, TestVo::getName));
        System.out.println(collect1);
    }

    @Data
    @AllArgsConstructor
    static
    class TestVo{
        private String age;
        private String name;
    }


}
