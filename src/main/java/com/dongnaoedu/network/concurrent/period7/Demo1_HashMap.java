package com.dongnaoedu.network.concurrent.period7;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/*
HashMapd是一个容器类，用来做Key-value存储
  存入的数据       可以按照key取出来
 */
public class Demo1_HashMap {
    public static void main(String args[]){

        Map<String, String> map = new HashMap<>();

        map.put("kody", "Kody is handsome");
        map.put("kody", "that is true...");

        map.put("tony", "tomy is strong");

        System.out.println(map.get("kody"));





        ConcurrentHashMap<String, String> map1 = new ConcurrentHashMap<>();




    }
}

class User{
    private String name;
    private int age;
    private boolean gender;

    public User(String name, int age, boolean gender){
        this.name = name;
        this.age = age;
        this.gender = gender;
    }

    @Override
    public String toString() {
        return String.format("name:%s, age:%d, gender: %b", name, age, gender);
    }
}



/*        int hash = 1000;
        int num1 = (16 - 1) & hash;

        int num2 = (32 - 1) & hash;

        int num3 = (64 - 1) & hash;

        System.out.println(num1);
        System.out.println(num2);
        System.out.println(num3);*/