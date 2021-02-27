package com.dongnaoedu.network;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// @EnableAsync // springboot全局异步，在这里加此注解，但是没有限制不建议这样用
public class Application {

    public static void main(String[] args) {
        System.out.println(111);
        SpringApplication.run(Application.class, args);
    }

}
